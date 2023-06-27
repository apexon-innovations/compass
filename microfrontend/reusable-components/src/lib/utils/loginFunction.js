import jwtDecode from 'jwt-decode'
import AES from 'crypto-js/aes'
import CryptoENC from 'crypto-js/enc-utf8'
import { postApiCall } from './apiCall'
const secretKey = 'compass@12345'

export const setRefreshToken = (forcefully = false) => {
  // Check if token refresh api is already called then return true
  if (localStorage.getItem('TOKEN_REFRESH_IN_PROCESS') === 'true') return false

  const tokenExpireTime = parseInt(localStorage.getItem('TOKEN_EXPIRES_WITHIN'))
  if (!tokenExpireTime) return false

  const minutes = 600000 // Ten minutes into milliseconds
  const now = Date.now()
  // Calculate current and token expire time difference if it's less-than or equal to five minutes then refresh token
  const diff = tokenExpireTime - now
  if (diff >= -1 && diff <= minutes) {
    refreshTokenApiCall()
  } else if (forcefully) {
    refreshTokenApiCall(forcefully)
  }
}

const refreshTokenApiCall = forcefully => {
  // Set token refresh api in process true to prevent multiple calls.
  localStorage.setItem('TOKEN_REFRESH_IN_PROCESS', true)
  const baseUrl = process.env.REACT_APP_API_END_POINT
  const refreshToken = getRefreshToken()
  postApiCall(`${baseUrl}/user-service/user/refresh`, { refreshToken: refreshToken }).then(
    responseData => {
      if (responseData.success && responseData.data && responseData.data.accessToken) {
        setIntoLocalStorage(responseData.data)
        localStorage.setItem('TOKEN_REFRESH_IN_PROCESS', null)
        if (forcefully) {
          setTimeout(() => {
            window.location.reload()
          }, 10)
        }
      } else {
        localStorage.setItem('TOKEN_REFRESH_IN_PROCESS', null)
        clearLocalStorage()
        window.location = '/login'
      }
    },
  )
}

export const clearLocalStorage = () => {
  localStorage.setItem('ACCESS_TOKEN', null)
  localStorage.setItem('TOKEN_EXPIRES_WITHIN', null)
  localStorage.setItem('REFRESH_TOKEN', null)
  localStorage.setItem('TOKEN_REFRESH_IN_PROCESS', null)
}

export const getLocalStorageValues = (key = false) => {
  try {
    if (localStorage.getItem('ACCESS_TOKEN') && localStorage.getItem('ACCESS_TOKEN') !== 'null') {
      // Decrypt AES jwt token
      const accessToken = AES.decrypt(localStorage.getItem('ACCESS_TOKEN'), secretKey).toString(
        CryptoENC,
      )

      // Decode jwt token to get information
      const decodedResponse = jwtDecode(accessToken)
      const roles = decodedResponse.rol.split(',')

      const info = {
        roles: roles,
        email: decodedResponse.usr,
        accessToken: accessToken,
      }

      if (key) return info[key]
      return info
    } else {
      return false
    }
  } catch (e) {
    clearLocalStorage()
    window.location = '/login'
  }
}

const getRefreshToken = () => {
  try {
    // Decrypt AES jwt token
    return AES.decrypt(localStorage.getItem('REFRESH_TOKEN'), secretKey).toString(CryptoENC)
  } catch (e) {
    clearLocalStorage()
    window.location = '/login'
  }
}

export const setIntoLocalStorage = ({ access_token, refresh_token, expires_in }) => {
  // Encrypt jwt token with AES and stored in localStorage
  let encryptedToken = AES.encrypt(access_token, secretKey)
  let encryptedRefreshToken = AES.encrypt(refresh_token, secretKey)

  // Set token expire time in milliseconds for easy to check when token is going to expire.
  // Date.now() in milliseconds + convert token expire minutes(response.expiresWithin) in milliseconds by multiplying 60000
  let tokenExpireTime = Date.now() + expires_in * 60000

  localStorage.setItem('ACCESS_TOKEN', encryptedToken)
  localStorage.setItem('REFRESH_TOKEN', encryptedRefreshToken)
  localStorage.setItem('TOKEN_EXPIRES_WITHIN', tokenExpireTime)

  return getLocalStorageValues()
}

export const isUserLogin = () => {
  return !!getLocalStorageValues('accessToken')
}
