import React, { Component } from 'react';
import { Slider, Handles, Tracks, Ticks, GetHandleProps, SliderItem, GetTrackProps, Rail } from 'react-compound-slider'
import styles from './TemplateSelectionSlider.module.scss';
 
interface TemplateSelectionSliderProps {
    valueMin: number;
    valueMax: number;
    minPieces: number;
    maxPieces: number;
    onChange(valueMin: number, valueMax: number): void;
}

interface TemplateSelectionSliderState {
    valueMin: number;
    valueMax: number;
}

class TemplateSelectionSlider extends Component<TemplateSelectionSliderProps, TemplateSelectionSliderState> {
    constructor(props: TemplateSelectionSliderProps) {
        super(props);
        this.state = {
            valueMin: props.valueMin,
            valueMax: props.valueMax
        }
    }

    componentDidUpdate(prevProps: TemplateSelectionSliderProps) {
        if (this.props.valueMin != prevProps.valueMin || 
            this.props.valueMax != prevProps.valueMax) {
                this.setState({
                    valueMin: this.props.valueMin,
                    valueMax: this.props.valueMax
                })
        }
    }

    onUpdate = (valueMin: number, valueMax: number) => {
        this.setState({valueMin, valueMax});
    }

    render() {
        const {valueMin, valueMax, minPieces, maxPieces, onChange} = this.props;
    
        return <div className={styles.mainContainer}>
            <label className={styles.sliderLabel}>pieces: {this.state.valueMin}-{this.state.valueMax}</label>
            <Slider
                className={styles.slider}
                mode={3}
                step={5}
                domain={[minPieces, maxPieces]}
                values={[valueMin, valueMax]}
                onChange={(values) => onChange(values[0], values[1])}
                onUpdate={(values) => this.onUpdate(values[0], values[1])}
            >    
                <Rail> 
                    {({ getRailProps }) => <React.Fragment>
                        <div className={styles.rail} {...getRailProps()}>
                            <div className={styles.railInner} />
                        </div>
                    </React.Fragment>
                    }
                </Rail>
                <Handles>
                    {({ handles, getHandleProps }) => (
                        <div className={styles.handlesContainer}>
                            { handles.map(handle => <Handle
                                    key={handle.id}
                                    handle={handle}
                                    domain={[0, 100]}
                                    getHandleProps={getHandleProps}
                                />
                            )}
                            {/* <div className={styles.valueMin}>{handles[0].value}</div>
                            <div className={styles.valueMax}>{handles[1].value}</div> */}
                        </div>
                    )}
                </Handles>
                <Tracks left={false} right={false}>
                    {({ tracks, getTrackProps }) => (
                    <div className={styles.tracksContainer}>
                        {tracks.map(({ id, source, target }) => (
                        <Track
                            key={id}
                            source={source}
                            target={target}
                            getTrackProps={getTrackProps}
                        />
                        ))}
                    </div>
                    )}
                </Tracks>
            </Slider>
            <div className={styles.domainMin}>{minPieces}</div>
            <div className={styles.domainMax}>{maxPieces}</div>
        </div>;
    }
}


interface HandleModel {
    id: string;
    value: number;
    percent: number;
}

interface HandleProps {
    domain: number[],
    handle: HandleModel;
    getHandleProps: GetHandleProps; 
}

const Handle = (handleProps: HandleProps) => {
    const {
        handle: {
            id, value, percent
        },
        domain: [min, max],
        getHandleProps
    } = handleProps;
    return <React.Fragment>
        <div
            role="slider"
            aria-valuemin={min}
            aria-valuemax={max}
            aria-valuenow={value}
            className={styles.handle}
            style={{'left': `${percent}%`}}
            {...getHandleProps(id)}
        >
            <div className={styles.handleInner} />
        </div>
    </React.Fragment>;
}

interface TrackProps {
    key: string;
    source: SliderItem;
    target: SliderItem;
    getTrackProps: GetTrackProps;
}

const Track = ({source, target, getTrackProps}: TrackProps) => {
    return <div
        className={styles.track}
        style={{
            left: `${source.percent}%`,
            width: `${target.percent - source.percent}%`
        }}
    {...getTrackProps()}
  />
}

interface TickProps {
    key: string;
    tick: SliderItem;
    count: number;
}

const Tick = ({key, count, tick}: TickProps) => (
    <div 
        key={key}
        style={{'left': `${tick.percent}%`}}
        className={styles.tick}
    >
        {tick.value}
    </div>
)

export default TemplateSelectionSlider;