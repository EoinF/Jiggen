import { Component } from "react";
import { connect } from "react-redux";
import { puzzleSolverActions } from "../../store/puzzleSolverScreen";
import { RouteComponentProps } from "react-router";
import qs from "query-string";

interface PlayPageQueryParams {
    backgroundImage?: string;
    background?: string;
    template?: string;
}

interface DispatchProps {
    activatePlayPage(): void;
    deactivatePlayPage(): void;
    selectTemplate(link: string): void;
    selectBackground(link: string, isCustom?: boolean): void;
}

type PlayPageProps = DispatchProps & RouteComponentProps;

class PlayPage extends Component<PlayPageProps> {
    componentDidMount() {
        const params = qs.parse(this.props.location.search) as PlayPageQueryParams;
        
        if (params.background) {
            this.props.selectBackground(params.background);
        } else if (params.backgroundImage) {
            this.props.selectBackground(params.backgroundImage, true);
        }
        if (params.template) {
            this.props.selectTemplate(params.template);
        }
        this.props.activatePlayPage();
    }

    componentWillUnmount() {
        this.props.deactivatePlayPage();
    }
    
    render() {
        return null;
    }
};

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
    return {
        activatePlayPage: () => dispatch(puzzleSolverActions.setIsActive(true)),
        deactivatePlayPage: () => dispatch(puzzleSolverActions.setIsActive(false)),
        selectTemplate: (link: string) => dispatch(puzzleSolverActions.selectAndDownloadTemplate(link)),
        selectBackground: (link: string, isCustom: boolean) => dispatch(puzzleSolverActions.selectAndDownloadBackground(link, isCustom))
    }
}


export default connect(undefined, mapDispatchToProps)(PlayPage);