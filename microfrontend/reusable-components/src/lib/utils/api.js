/*eslint no-useless-catch: "error"*/
/* eslint-disable no-undef */
import Promise from 'bluebird'
import { getLocalStorageValues } from './loginFunction'

// There is lots of Data in some API so we need to add more timeout here
const TIMEOUT = 60000

/**
 * GET a path relative to API root url.
 * @param {String}  path Relative path to the configured API endpoint
 * @returns {Promise} of response body
 */
export async function get(path) {
  return bodyOf(request('get', path, null))
}

/**
 * POST JSON to a path relative to API root url
 * @param {String} path Relative path to the configured API endpoint
 * @param {Object} body Anything that you can pass to JSON.stringify
 * @returns {Promise}  of response body
 */
export async function post(path, body) {
  return bodyOf(request('post', path, body))
}

/**
 * PUT JSON to a path relative to API root url
 * @param {String} path Relative path to the configured API endpoint
 * @param {Object} body Anything that you can pass to JSON.stringify
 * @returns {Promise}  of response body
 */
export async function put(path, body) {
  return bodyOf(request('put', path, body))
}

/**
 * DELETE a path relative to API root url
 * @param {String} path Relative path to the configured API endpoint
 * @returns {Promise}  of response body
 */
export async function del(path) {
  return bodyOf(request('delete', path, null))
}

/**
 * Make arbitrary fetch request to a path relative to API root url
 * @param {String} method One of: get|post|put|delete
 * @param {String} path Relative path to the configured API endpoint
 * @param {Object} body Anything that you can pass to JSON.stringify
 */
export async function request(method, path, body) {
  // eslint-disable-next-line no-useless-catch
  try {
    const response = await sendRequest(method, path, body)
    return handleResponse(path, response)
  } catch (error) {
    const err = error
    throw err
  }
}

/**
 * Takes a relative path and makes it a full URL to API server
 */
export function url(path) {
  if (path.includes('http://') || path.includes('https://') || path.includes('//')) {
    return path
  } else {
    const apiRoot = process.env.REACT_APP_API_END_POINT
    return path.indexOf('/') === 0 ? apiRoot + path : apiRoot + '/' + path
  }
}

/**
 * Constructs and fires a HTTP request
 */
async function sendRequest(method, path, body) {
  try {
    const endpoint = url(path)
    const token = getLocalStorageValues('accessToken')
      ? `Bearer ${getLocalStorageValues('accessToken')}`
      : ''
    const email = getLocalStorageValues('email') ? getLocalStorageValues('email') : ''

    const headers = getRequestHeaders(body, token, email)
    const options = body ? { method, headers, body: JSON.stringify(body) } : { method, headers }

    return timeout(fetch(endpoint, options), TIMEOUT)
  } catch (e) {
    const error = e
    throw new Error(error)
  }
}

/**
 * Receives and reads a HTTP response
 */
async function handleResponse(_path, response) {
  // eslint-disable-next-line no-useless-catch
  try {
    const responseBody = await response.text()
    const responseBodyJson = {}
    responseBodyJson.status = response.status
    // eslint-disable-next-line no-useless-catch
    try {
      responseBodyJson.data = responseBody ? JSON.parse(responseBody) : null
    } catch (e) {
      const error = e
      throw error
    }
    return {
      status: response.status,
      headers: response.headers,
      body: responseBodyJson ? responseBodyJson : null,
    }
  } catch (e) {
    const error = e
    throw error
  }
}

function getRequestHeaders(body, token, email) {
  const headers = body
    ? { Accept: 'application/json', 'Content-Type': 'application/json' }
    : { Accept: 'application/json' }

  if (token && email) {
    return { ...headers, Authorization: token, 'X-User-Header': email }
  }

  return headers
}

/**
 * Rejects a promise after `ms` number of milliseconds, it is still pending
 */
function timeout(promise, ms) {
  return new Promise((resolve, reject) => {
    const timer = setTimeout(() => reject(new Error('timeout')), ms)
    promise
      .then(response => {
        clearTimeout(timer)
        resolve(response)
      })
      .catch(reject)
  })
}

async function bodyOf(requestPromise) {
  // eslint-disable-next-line no-useless-catch
  try {
    const response = await requestPromise
    return response.body
  } catch (e) {
    const error = e
    throw error
  }
}
