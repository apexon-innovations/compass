#! /usr/bin/env node
var shell = require('shelljs')
const os = require('os')
const RC_COMMON_PACKAGE_NAME = 'reusable-components'

const osType = os.type()

if (osType.includes('Windows')) {
  if (!shell.which('concurrently')) {
    shell.exec('npm i concurrently -g')
  }

  shell.exec(
    `concurrently "cd ../des-reusable-components && npm link && npm run start:watch:win" "cd ../des-data-utilities && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start:win" "cd ../des-login && npm run start:win"`,
  )
} else {
  shell.exec(
    `(cd ../des-reusable-components && npm link && npm run start:watch) & (cd ../des-login && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start) & (cd ../des-data-utilities && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start)`,
  )
}
