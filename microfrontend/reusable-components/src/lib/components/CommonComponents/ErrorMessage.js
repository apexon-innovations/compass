import React from 'react'
import PropTypes from 'prop-types'

const ErrorMessage = React.memo(({ error }) => {
  let { message, errorCode, isMsgPassed } = error

  switch (errorCode) {
    case 403:
      window.location = '/error-page'
      break
    case 404:
      message = isMsgPassed ? message : 'Sorry, No Data Available.'
      break
    default:
      break
  }

  return (
    <div className="noDataBox">
      <div className="text">{message ? message : 'Something went wrong'}</div>
    </div>
  )
})
ErrorMessage.propTypes = {
  error: PropTypes.any,
}

export default ErrorMessage
