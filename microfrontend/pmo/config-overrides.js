const rewireAliases = require('react-app-rewire-aliases')
const path = require('path')
const ModuleScopePlugin = require('react-dev-utils/ModuleScopePlugin');

/* config-overrides.js */
module.exports = function override(config, env) {
  config.resolve.plugins = config.resolve.plugins.filter(plugin => !(plugin instanceof ModuleScopePlugin));
  config = rewireAliases.aliasesOptions({
    react: path.resolve('./node_modules/react'),
    'react-router': path.resolve('./node_modules/react-router'),
    'react-router-dom': path.resolve('./node_modules/react-router-dom'),
    'react-dom': path.resolve('./node_modules/react-dom'),
    'react-notifications': path.resolve('./node_modules/react-notifications'),
  })(config, env)
  return config
}
