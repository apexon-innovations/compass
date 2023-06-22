import { useState, useEffect } from 'react'
import { getApiCall } from '../../utils/apiCall'

const useWidgetAPIWrapper = params => {
  const { apiEndPointUrl, data, callback, requiredParams, errorMessage } = params
  const responseMapFunction = params.responseMapFunction || false
  const paramToPassInMapFunction = params.paramToPassInMapFunction || false
  const [apiStatus, setApiStatus] = useState({
    response: data || false,
    error: {},
    isLoading: true,
    isValid: true,
  })

  useEffect(() => {
    let isValid = true
    requiredParams &&
      requiredParams.map(param => {
        let key = Object.keys(param)
        if (!param[key[0]]) {
          isValid = false
        }
        return param
      })

    if (!isValid) {
      setApiStatus({
        response: false,
        error: {
          errorCode: 402,
          message: errorMessage ? errorMessage : 'You must need to pass required fields..!',
        },
        isLoading: false,
        isValid: false,
      })
    } else if ((!data || !(Object.keys(data).length !== 0 || data.length > 0)) && !apiEndPointUrl) {
      setApiStatus({
        response: false,
        error: { errorCode: 402, message: 'You must need to pass data or api end-point..!' },
        isLoading: false,
      })
    } else if (!data && apiEndPointUrl) {
      setApiStatus({
        response: false,
        error: {},
        isLoading: true,
      })
      getApiCall(apiEndPointUrl).then(responseData => {
        if (responseData.success) {
          let resData = responseMapFunction
            ? paramToPassInMapFunction
              ? responseMapFunction(responseData.data, paramToPassInMapFunction)
              : responseMapFunction(responseData.data)
            : responseData.data
          if (callback) callback(resData, responseData.data)
          setApiStatus({
            response: resData,
            error: {},
            isLoading: false,
          })
        } else if (responseData.data.errorCode === 401) {
          setApiStatus({
            response: false,
            error: responseData.data,
            isLoading: true,
          })
        } else {
          setApiStatus({
            response: false,
            error: responseData.data,
            isLoading: false,
          })
        }
      })
    } else {
      setApiStatus({
        response: data || false,
        error: {},
        isLoading: false,
      })
    }
  }, [
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
    requiredParams,
    errorMessage,
    paramToPassInMapFunction,
  ])

  return { response: apiStatus.response, error: apiStatus.error, isLoading: apiStatus.isLoading }
}

export default useWidgetAPIWrapper
