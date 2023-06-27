import React from 'react'
import PropTypes from 'prop-types'
import ErrorMessage from '../CommonComponents/ErrorMessage'
import Loader from '../Loader/Loader'

const WidgetContainer = React.memo(
  ({
    isLoading,
    data = {},
    error,
    children,
    errorClass = false,
    errorMessage = true,
    loadingComponent,
  }) => {
    if (isLoading) {
      return loadingComponent ? loadingComponent : <Loader />
    }

    if (error && !(Object.keys(data).length !== 0 || data.length > 0)) {
      return errorMessage ? (
        <div className={errorClass ? errorClass : ''}>
          <ErrorMessage error={error} />
        </div>
      ) : (
        ''
      )
    }

    if ((data && Object.keys(data).length !== 0) || data.length > 0) {
      return children
    }
  },
)

WidgetContainer.propTypes = {
  isLoading: PropTypes.bool,
  data: PropTypes.any,
  error: PropTypes.object,
  children: PropTypes.any,
  errorClass: PropTypes.any,
  errorMessage: PropTypes.bool,
  loadingComponent: PropTypes.any,
}

export default WidgetContainer
