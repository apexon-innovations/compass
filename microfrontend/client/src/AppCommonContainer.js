import React from 'react'
import PropTypes from 'prop-types'
import DashboardSwitchForTwo from './components/DashboardSwitchForTwo/DashboardSwitchForTwo'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'
import { matchPath } from 'react-router'

const AppCommonContainer = React.memo(props => {
  const VALID_ROUTES_TO_SHOW_DS_PS = ['business-overview', 'overview']

  const getProjectIdFromParams = () => {
    const param = matchPath({
      path: '/client/:basePath',
    }, props.location.pathname)
    return param && param.params ? param.params : {}
  }

  const params = getProjectIdFromParams();
  return (
    <React.Fragment>
      {VALID_ROUTES_TO_SHOW_DS_PS.includes(params.basePath) && (
        <DashboardSwitchForTwo
          navigate={props.navigate}
          key={params.projectId}
          basePath={params.basePath}
          projectId={params.projectId}
        />
      )}
    </React.Fragment>
  )
})

AppCommonContainer.propTypes = {
  children: PropTypes.any,
  projectId: PropTypes.any,
  location: PropTypes.any,
  basePath: PropTypes.any,
  navigate: PropTypes.any,
}

export default withRouter(AppCommonContainer)
