import React from 'react'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './SecurityScoreWidget.module.scss'

const SecurityScoreWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })

  return (
    <BracketBox animStatus="" boxTitle={'Security Score'}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && (
          <div className={[style.securityScore, 'blurAll'].join(' ')}>
            <ScoreBox title={'CWE'} value={'3247'} />
            <ScoreBox title={'OWASP'} value={'1274'} />
            <ScoreBox title={'GDPR'} value={'1952'} />
            <ScoreBox title={'SQL Injection'} value={'2021'} />
            <ScoreBox title={'XSS'} value={'3091'} />
            <ScoreBox title={'Command Injection'} value={'0672'} />
          </div>
        )}
      </WidgetContainer>
    </BracketBox>
  )
})

const ScoreBox = ({ title, value }) => {
  return (
    <div className={style.box}>
      <div className={style.title}>{title}</div>
      <div className={style.value}>{value}</div>
    </div>
  )
}

ScoreBox.propTypes = {
  title: PropTypes.string,
  value: PropTypes.any,
}

SecurityScoreWidget.propTypes = {
  callback: PropTypes.func,
  colorName: PropTypes.string,
  sortOption: PropTypes.string,
}

SecurityScoreWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default SecurityScoreWidget
