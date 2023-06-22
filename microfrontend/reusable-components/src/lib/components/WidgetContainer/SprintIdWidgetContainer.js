import React from 'react'
import PropTypes from 'prop-types'
import WidgetContainer from './WidgetContainer'
import Loader from '../Loader/Loader'

const SprintIdWidgetContainer = React.memo(({ children, sprintId }) => {
  if (sprintId && sprintId !== 'none') {
    return children
  } else if (sprintId === 'none') {
    return <WidgetContainer error={{ errorCode: 404 }} />
  } else {
    return <Loader />
  }
})

SprintIdWidgetContainer.propTypes = {
  children: PropTypes.any,
  sprintId: PropTypes.any,
}

export default SprintIdWidgetContainer
