#! /usr/bin/env node
var shell = require('shelljs')
const os = require('os')

const osType = os.type()

if (osType.includes('Windows')) {
    if (!shell.which('concurrently')) {
        shell.exec('npm i concurrently -g')
    }
    //windows
    shell.exec(
        `concurrently "cd reusable-components && npm install" "cd pmo && npm install" "cd login && npm install" "cd client && npm install"`,
    )
} else {
    //mac
    shell.exec(
        `(cd reusable-components && npm install --legacy-peer-deps) & (cd login && npm install --legacy-peer-deps) & (cd pmo && npm install --legacy-peer-deps) & (cd client && npm install --legacy-peer-deps)`,
    )
}
