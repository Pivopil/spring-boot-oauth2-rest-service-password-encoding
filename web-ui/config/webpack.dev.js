var webpackMerge = require('webpack-merge');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var commonConfig = require('./webpack.common.js');
var helpers = require('./helpers');

var targetUrl = 'http://localhost:8065';

module.exports = webpackMerge(commonConfig, {
    devtool: 'cheap-module-eval-source-map',

    output: {
        path: helpers.root('dist'),
        publicPath: 'http://localhost:8083/',
        filename: '[name].js',
        chunkFilename: '[id].chunk.js'
    },

    plugins: [
        new ExtractTextPlugin('[name].css')
    ],

    devServer: {
        historyApiFallback: true,
        stats: 'minimal',
        proxy: {
            '/oauth/*': {
                target: targetUrl,
                changeOrigin: true,
                ws: false
            },
            '/api/*': {
                target: targetUrl,
                changeOrigin: true,
                ws: false
            },
            '/stomp/*': {
                target: targetUrl,
                changeOrigin: true,
                ws: true
            }
        }
    }
});