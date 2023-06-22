module.exports = {
  env: {
    browser: true,
    node: true, //For access .env process variables
    es6: true,
  },
  extends: ['eslint:recommended', 'plugin:react/recommended', 'react-app'],
  globals: {
    Atomics: 'readonly',
    SharedArrayBuffer: 'readonly',
  },
  parser: '@babel/eslint-parser',
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 2018,
    sourceType: 'module',
    allowImportExportEverywhere: true,
  },
  plugins: ['react'],
  rules: {
    'react/no-unescaped-entities': 'off',
    'react/display-name': 'off',
    'no-console': 'error',
  },
}
