import React, { Component } from 'react';
import { connect } from 'react-redux';
import PlainLink from '../utils/PlainLink';
import { templatesActions } from '../actions';

import StickyHeader from './StickyHeader';
import TemplateWidget from './TemplateWidget';
 
class TemplateSelection extends Component {
	componentDidMount() {
		const {
			fetchTemplates,
			templates
		} = this.props;

		console.log(templates.length);
		
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

		console.log("props");
		console.log(this.props);
		return (
			<div>
	            <PlainLink to={`/`} >
					<StickyHeader className="templateTableHeader">
						<span>{"◄ "}</span>
						<span>Choose a Template</span>
					</StickyHeader>
					<div className="templateHeaderBuffer" />
				</PlainLink>
				<div className="templateTable">
				{ templates && templates.map(template => { return (
	            		<PlainLink to={`/`} key={template.id} style={{ textDecoration: 'none' }}>
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