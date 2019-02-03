#version 100

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 uv_overlay;

uniform vec2 u_texture_texel_size;
uniform float u_camera_zoom;
uniform sampler2D u_texture;
uniform sampler2D u_background;

bool isTopEdge() {
    float newY = v_texCoords.y + u_texture_texel_size.y * u_camera_zoom;
    vec4 template_colour_up = texture2D(u_texture, vec2(v_texCoords.x, newY));
    return newY > 1.0 || template_colour_up.a == 0.0;
}
bool isBottomEdge() {
    float newY = v_texCoords.y - u_texture_texel_size.y * u_camera_zoom;
    vec4 template_colour_down = texture2D(u_texture, vec2(v_texCoords.x, newY));
    return newY < 0.0 || template_colour_down.a == 0.0;
}
bool isLeftEdge() {
    float newX = v_texCoords.x - u_texture_texel_size.x * u_camera_zoom;
    vec4 template_colour_left = texture2D(u_texture, vec2(newX, v_texCoords.y));
    return newX < 0.0 || template_colour_left.a == 0.0;
}
bool isRightEdge() {
    float newX = v_texCoords.x + u_texture_texel_size.x * u_camera_zoom;
    vec4 template_colour_right = texture2D(u_texture, vec2(newX, v_texCoords.y));
    return newX > 1.0 || template_colour_right.a == 0.0;
}

void main()
{
    vec4 template_colour = texture2D(u_texture, v_texCoords);

    // If the colour isn't transparent
    if (template_colour.a != 0.0) {
        vec4 combination_colour = vec4(1.0, 1.0, 1.0, 1.0);
        if (isTopEdge() || isBottomEdge() || isLeftEdge() || isRightEdge()) {
            combination_colour = vec4(0.2, 0.2, 0.2, 1.0);
        }
        vec4 background_colour = texture2D(u_background, uv_overlay);
        vec4 combined_colour = background_colour * combination_colour;
        gl_FragColor = v_color * combined_colour;
    } else {
        gl_FragColor = v_color * template_colour;
    }
}
