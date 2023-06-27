import React, { useState, useEffect } from 'react'
import PropTypes from 'prop-types'
import DashboardSwitch from './components/DashboardSwitch/DashboardSwitch'
import ProjectSliderWidget from 'reusable-components/dist/widgets/ProjectSliderWidget/ProjectSliderWidget'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'

import { matchPath } from 'react-router'

const AppCommonContainer = React.memo(props => {
  const VALID_ROUTES_TO_SHOW_DS_PS = ['operational', 'overview', 'strategic']
  const baseUrl = process.env.REACT_APP_API_END_POINT
  const [projectReRenderKey, setProjectReRenderKey] = useState(1)

  const getProjectIdFromParams = () => {
    const param = matchPath({
      path: '/pmo/:basePath/:projectId',
    }, props.location.pathname)
    return param && param.params ? param.params : {}
  }

  useEffect(() => {
    window.addEventListener('updateProjectIcon', () => {
      setProjectReRenderKey(Math.random())
    })
  }, [])

  const params = getProjectIdFromParams();

  return (
    <React.Fragment>
      {params.projectId && VALID_ROUTES_TO_SHOW_DS_PS.includes(params.basePath) && (
        <DashboardSwitch key={params.projectId} basePath={params.basePath} projectId={params.projectId} />
      )}
      {params.projectId && VALID_ROUTES_TO_SHOW_DS_PS.includes(params.basePath) && (
        <ProjectSliderWidget
          apiEndPointUrl={`${baseUrl}/psr-service/project/all`}
          projectId={params.projectId}
          basePath={params.basePath}
          key={projectReRenderKey}
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
}

export default withRouter(AppCommonContainer)
