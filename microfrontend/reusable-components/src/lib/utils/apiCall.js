import { get, post, put } from './api'
import { setRefreshToken, clearLocalStorage } from './loginFunction'
import { NotificationErrorWrapper } from './commonFunction'

export const getApiCall = apiEndPointUrl => {
  return get(apiEndPointUrl)
    .then(responseData => {
      if (responseData.status === 200) {
        return { success: true, data: responseData.data }
      } else if (responseData.status === 201) {
        return { success: true, data: responseData.data }
      } else {
        if (responseData.status === 401) {
          setRefreshToken(true)
        } else if (responseData.status === 403) {
          clearLocalStorage()
        }
        return {
          success: false,
          data: {
            errorCode: responseData.status,
            message: responseData.data.apiError
              ? responseData.data.apiError.message
              : responseData.data.message,
            apiError: responseData.data.apiError ? responseData.data.apiError : responseData.data,
          },
        }
      }
    })
    .catch(e => {
      NotificationErrorWrapper()
      return { success: false, data: { errorCode: e.status, message: e.message, apiError: e } }
    })
}

export const postApiCall = (apiEndPointUrl, apiPostData) => {
  return post(apiEndPointUrl, apiPostData)
    .then(responseData => {
      setRefreshToken()
      if (responseData.status === 200) {
        return { success: true, data: responseData.data }
      } else if (responseData.status === 201) {
        return { success: true, data: responseData.data }
      } else if (responseData.status === 204) {
        return { success: true, data: {} }
      } else {
        if (responseData.status === 401) {
          setRefreshToken(true)
        }
        return {
          success: false,
          data: {
            errorCode: responseData.status,
            message: responseData.data.apiError
              ? responseData.data.apiError.message
              : responseData.data.message,
            apiError: responseData.data.apiError ? responseData.data.apiError : responseData.data,
          },
        }
      }
    })
    .catch(e => {
      NotificationErrorWrapper()
      return { success: false, data: { errorCode: e.status, message: e.message, apiError: e } }
    })
}

export const putApiCall = (apiEndPointUrl, apiPostData) => {
  return put(apiEndPointUrl, apiPostData)
    .then(responseData => {
      setRefreshToken()
      if (responseData.status === 200) {
        return { success: true, data: responseData.data }
      } else if (responseData.status === 201) {
        return { success: true, data: responseData.data }
      } else {
        if (responseData.status === 401) {
          setRefreshToken(true)
        }
        return {
          success: false,
          data: {
            errorCode: responseData.status,
            message: responseData.data.apiError
              ? responseData.data.apiError.message
              : responseData.data.message,
            apiError: responseData.data.apiError ? responseData.data.apiError : responseData.data,
          },
        }
      }
    })
    .catch(e => {
      NotificationErrorWrapper()
      return { success: false, data: { errorCode: e.status, message: e.message, apiError: e } }
    })
}
