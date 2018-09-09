import React, { Component } from 'react';
import { connect } from 'react-redux';
import PlainLink from '../../widgets/PlainLink';
import { templatesActions } from '../../actions';

import { JiggenHeader } from '../OverviewScreen'
import TemplateWidget from './TemplateWidget';

import './templateSelection.css';
 
class TemplateSelection extends Component {
	componentDidMount() {
		const {
			fetchTemplates,
			templates
		} = this.props;
		
		if (templates.length === 0) {
			fetchTemplates();
		}
	}

	onSelectTemplate = (e, id) => {
		this.props.onSelectTemplate(id);
	};

	render() {
		const {
			templates,
			selectedTemplateId
		} = this.props;

		return (
			<div className="templateSelection">
	            <PlainLink to={`/`} >
					<JiggenHeader>
						<h1>
							<span>{"◄ "}</span>
							<span>Choose a Template</span>
						</h1>
					</JiggenHeader>
				</PlainLink>
				<div className="templateTable">
				{ templates && templates.map(template => { return (
	            		<PlainLink to={`/`} key={template.id} >
	            			<TemplateWidget
	            				template={template}
	            				isSelected={template.id === selectedTemplateId}
	            				onClick={(e) => this.onSelectTemplate(e, template.id)}
	            			/>
	            		</PlainLink>
					)})
				}
				</div>
			</div>
		);
	}
}

const mapStateToProps = state => {
  return {
    templates: state.templates.templates,
    selectedTemplateId: state.templates.selectedId
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchTemplates: id => {
    	dispatch(templatesActions.fetchTemplates())
    },
    onSelectTemplate: id => {
    	dispatch(templatesActions.selectTemplate(id))
    }
  }
}

const ConnectedTemplateSelection = connect(
	mapStateToProps,
	mapDispatchToProps
)(TemplateSelection);

export default ConnectedTemplateSelection;