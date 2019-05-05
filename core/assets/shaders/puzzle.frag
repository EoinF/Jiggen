#version 100

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 uv_overlay;

uniform vec2 u_texture_texel_size;
uniform vec4 u_texture_region_bounds;
uniform float u_camera_zoom;
uniform sampler2D u_texture;
uniform sampler2D u_background;

const float inverseTotalPixelChecks = 1.0/4.0;

float isEdgeOfTexture(vec2 direction) {
    vec2 texelToCheck = v_texCoords + ((u_texture_texel_size * direction) * u_camera_zoom);

    if (texelToCheck.x < u_texture_region_bounds.x
        || texelToCheck.x > u_texture_region_bounds.z
        || texelToCheck.y < u_texture_region_bounds.y
        || texelToCheck.y > u_texture_region_bounds.w
    ) {
        return 1.0;
    }
    else {
        vec4 texelColor = texture2D(u_texture, texelToCheck);
        return 1.0 - texelColor.a;
    }
}

void main()
{
    vec4 template_colour = texture2D(u_texture, v_texCoords);

    vec4 combination_colour = vec4(1.0, 1.0, 1.0, template_colour.a);
    float total = 0.0;
    total += isEdgeOfTexture(vec2(1.0, 0.0));
    total += isEdgeOfTexture(vec2(-1.0, 0.0));
    total += isEdgeOfTexture(vec2(0.0, 1.0));
    total += isEdgeOfTexture(vec2(0.0, -1.0));

    combination_colour.rgb *= 1.0 - smoothstep(0.15, 0.3, total * inverseTotalPixelChecks);

    vec4 background_colour = texture2D(u_background, uv_overlay);
    vec4 combined_colour = background_colour * combination_colour;
    gl_FragColor = v_color * combined_colour;
}

