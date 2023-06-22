import React from 'react'
import PropTypes from 'prop-types'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import HalfCircleNumber from '../../components/HalfCircleNumber/HalfCircleNumber'
import ModernBox from '../../components/CommonComponents/ModernBox/ModernBox'
import style from './BlockersWidget.module.scss'

const responseMap = response => {
  return response && response.data ? response.data.blockers : {}
}

const BlockersWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMap,
  })
  return (
    <ModernBox animStatus="" boxTitle={'Blockers'}>
      <WidgetContainer isLoading={isLoading} data={response} error={error}>
        <HalfCircleNumber className={style.blockerNumber} value={response.total} />
      </WidgetContainer>
    </ModernBox>
  )
})

BlockersWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default BlockersWidget
