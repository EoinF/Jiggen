#version 100

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 uv_overlay;

uniform sampler2D u_texture;
uniform sampler2D u_background;

void main()
{
    vec4 template_colour = texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y));
    vec4 background_colour = texture2D(u_background, vec2(uv_overlay.x, uv_overlay.y));

    // If the colour is black, then use gray (so the background is visible when combined)
    if (template_colour == vec4(0, 0, 0, 1)) {
        template_colour = vec4(0.1, 0.1, 0.1, 1);
    }
    vec4 combined_colour = vec4(template_colour.r * background_colour.r,
    template_colour.g * background_colour.g, template_colour.b * background_colour.b, template_colour.a);

    gl_FragColor = v_color * combined_colour;
}