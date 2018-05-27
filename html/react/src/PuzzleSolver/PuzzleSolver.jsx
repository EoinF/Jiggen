import React, { Component } from 'react';
import { connect } from 'react-redux';
import { generatedTemplatesActions } from '../actions';

class PuzzleSolver extends Component {
	componentDidMount() {
		const { 
			match: { params },
			fetchGeneratedTemplateById,
			generatedTemplate,
		} = this.props;
		
		if (!generatedTemplate) {
			fetchGeneratedTemplateById(params.id);
		}

		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.remove('hidden');
		react_root.classList.add('hidden');
	}

	render() {
		const {
			generatedTemplate, 
			generatedTemplateId
		} = this.props;
		
		return (
			<div>
				<div>{generatedTemplateId}</div>
				{ generatedTemplate && 
					<div><img src={generatedTemplate.links.image} alt="none found"/></div>
				}
			</div>
		);
	}
}


const mapStateToProps = state => {
  return {
    generatedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId],
    generatedTemplateId: state.generatedTemplates.selectedId
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplateById: id => {
      dispatch(generatedTemplatesActions.fetchGeneratedTemplateById(id))
    }
  }
}

const ConnectedPuzzleSolver = connect(
	mapStateToProps,
	mapDispatchToProps
)(PuzzleSolver);

export default ConnectedPuzzleSolver;