import React from 'react'
import { useLocation, useNavigate, useParams } from 'react-router-dom'

export const withRouter = Component => props => (
  <Component navigate={useNavigate()} params={useParams()} location={useLocation()} {...props} />
)
