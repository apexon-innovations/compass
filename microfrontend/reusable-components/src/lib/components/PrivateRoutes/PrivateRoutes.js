import React from 'react'
import { Navigate } from 'react-router-dom'
import PropTypes from 'prop-types'
import Header from '../Header/Header'
import Footer from '../Footer/Footer'
import { getUserRoles, arraysEqual } from '../../utils/commonFunction'
import { getLocalStorageValues } from '../../utils/loginFunction'

const PrivateRoutes = React.memo(({ children, requiredRoles, isSearchBox, ...rest }) => {
  const isAuthed = !!getLocalStorageValues('accessToken')
  const role = getUserRoles()
  let userHasRequiredRole
  if (role) {
    userHasRequiredRole = arraysEqual(requiredRoles, role)
  }

  return isAuthed && userHasRequiredRole ? (
    <React.Fragment>
      <Header isSearchBox={isSearchBox} />
      {children}
      <Footer />
    </React.Fragment>
  ) : (
    <Navigate to={'/?loggedOut=invalidUser'} />
  )
})

PrivateRoutes.propTypes = {
  children: PropTypes.any,
  requiredRoles: PropTypes.any,
  isSearchBox: PropTypes.bool,
}

export default PrivateRoutes
