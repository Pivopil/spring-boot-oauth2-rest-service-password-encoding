import ReactDOM from "react-dom";
import React, {Component} from "react";
import window from "global/window";
import document from "global/document";

// import ComplexChartExample from './complex-chart-example';
// import '../../src/styles/examples.scss';

export default class App extends Component {
    componentWillMount() {
        window.addEventListener(
            'resize',
            () => this.setState({width: window.innerWidth})
        );
    }

    render() {
        return (
            <article>
                <h1>Simple Complex Chart Example</h1>
                <section>
                    Hello!!!
                </section>
            </article>
        );
    }
}

ReactDOM.render(<App />, document.querySelector('#index'));
