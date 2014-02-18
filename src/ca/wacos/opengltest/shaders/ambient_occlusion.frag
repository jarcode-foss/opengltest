
uniform sampler2D tex0;
uniform sampler2D tex1;

uniform float zNear;
uniform float zFar;

float linearizeDepth(float zoverw) {
		return (2.0 * zNear) / (zFar + zNear - zoverw * (zFar - zNear));
}

void main() {
	vec2 uv = gl_TexCoord[0].st;

	float d = linearizeDepth(texture2D(tex1, uv).r);

	gl_FragColor = texture2D(tex0, uv);
}