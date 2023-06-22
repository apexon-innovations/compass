#! /usr/bin/env node
var shell = require('shelljs')
const os = require('os')
const RC_COMMON_PACKAGE_NAME = 'reusable-components'

const osType = os.type()

if (osType.includes('Windows')) {
    if (!shell.which('concurrently')) {
        shell.exec('npm i concurrently -g')
    }
    //windows
    shell.exec(
        `concurrently "cd ../reusable-components && npm link && npm run start:watch:win" "cd ../pmo && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start:win" "cd ../login && npm run start:win" "cd ../pmo && npm run start:win" cd ../client && npm run start:win"`,
    )
} else {
    //mac
    shell.exec(
        `(cd reusable-components && npm link && npm run start:watch) & (cd login && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start) & (cd pmo && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start) & (cd client && npm link ${RC_COMMON_PACKAGE_NAME} && npm run start)`,
    )
}
