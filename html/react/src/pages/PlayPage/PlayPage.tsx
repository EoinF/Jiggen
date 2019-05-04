import { Component } from "react";
import { connect } from "react-redux";
import { puzzleSolverActions } from "../../store/puzzleSolverScreen";
import { RouteComponentProps } from "react-router";
import qs from "query-string";

interface PlayPageQueryParams {
    background?: string;
    template?: string;
}

interface DispatchProps {
    activatePlayPage(): void;
    deactivatePlayPage(): void;
    selectTemplate(link: string): void;
    selectBackground(link: string): void;
}

type PlayPageProps = DispatchProps & RouteComponentProps;

class PlayPage extends Component<PlayPageProps> {
    componentDidMount() {
        const params = qs.parse(this.props.location.search) as any as PlayPageQueryParams;
        
        if (params.background && params.template) {
            this.props.selectBackground(params.background);
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
        selectBackground: (link: string) => dispatch(puzzleSolverActions.selectAndDownloadBackground(link))
    }
}


export default connect(undefined, mapDispatchToProps)(PlayPage);