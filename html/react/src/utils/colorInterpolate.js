import parse from 'color-parse';
import hsl from 'color-space/hsl';
import lerp from 'lerp';
import clamp from 'mumath/clamp';

const generateColourMap = (palette) => {
	palette = palette.map(c => {
		c = parse(c);
		if (c.space !== 'rgb') {
			if (c.space !== 'hsl') {
			    throw Error(`${c.space} space is not supported.`);
			}
			c.values = hsl.rgb(c.values);
		}
		c.values.push(c.alpha);
		return c.values;
	});

	return function(t, mix = lerp) {
		t = clamp(t, 0, 1);

		let idx = ( palette.length - 1 ) * t,
			lIdx = Math.floor( idx ),
			rIdx = Math.ceil( idx );

		t = idx - lIdx;

		let lColor = palette[lIdx], rColor = palette[rIdx];

		let result = lColor.map((v, i) => {
			v = mix(v, rColor[i], t);
			if (i < 3) v = Math.round(v);
			return v;
		});

		if (result[3] === 1) {
			return `rgb(${result.slice(0,3)})`;
		}
		return `rgba(${result})`;
	};
}

export default generateColourMap;