import { Component } from "react";
import { connect } from "react-redux";
import { puzzleSolverActions } from "../../store/puzzleSolverScreen";

interface DispatchProps {
    activatePlayPage(): void;
    deactivatePlayPage(): void;
}

type PlayPageProps = DispatchProps;

class PlayPage extends Component<PlayPageProps> {
    componentDidMount() {
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
        deactivatePlayPage: () => dispatch(puzzleSolverActions.setIsActive(false))
    }
}


export default connect(undefined, mapDispatchToProps)(PlayPage);