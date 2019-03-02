import React, { Component } from "react";
import { from } from "rxjs";
import gwtAdapter from "../../gwtAdapter";
import styles from './GameContainer.module.scss';

import puzzlePieceIcon from './piece-outline-rounded.png';

interface LoadingDisplayState {
    isLoading: Boolean;
}

export default function withLoadingWrapper<P>(BaseComponent: React.ComponentType<P>) {
    return class LoadingDisplayWrapper extends Component<P, LoadingDisplayState> {
        state = {
            isLoading: true
        }

        componentDidMount() {
            const onGwtLoaded$ = from(gwtAdapter.onGwtLoadedPromise);

            onGwtLoaded$.subscribe(this.onLoad);
        }

        onLoad = () => {
            this.setState({
                isLoading: false
            });
        }

        render() {
            if (this.state.isLoading) {
                return <div className={styles.mainContainer}>
                    <div className={styles.loadingContainer}>
                        <div>Unpacking the box...</div>
                        <img width='32px' height='32px'
                            src={puzzlePieceIcon}
                            className={styles.loadingSpinner}
                            alt='Loading spinner'
                        />
                    </div>
                </div>
            } else {
                return <BaseComponent {...this.props} />
            }
        }
    }
}
