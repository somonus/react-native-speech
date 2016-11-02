/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
import { Router, Route } from './src';
import Test from './test';
import Demo from './demo';

export default class app2 extends Component {
  render() {
    return (
      <Router>
        <Route path='root' component={Test}>
          <Route path='demo' component={Demo} animation='FloatFromRight'>
            <Route path='demo' component={Demo} animation='FloatFromLeft'></Route>
          </Route>
        </Route>
        <Route path='demo' component={Demo} animation='FloatFromBottom'>
          <Route path='demo' component={Demo}></Route>
        </Route>
      </Router>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 30,
  },
});

AppRegistry.registerComponent('app2', () => app2);
