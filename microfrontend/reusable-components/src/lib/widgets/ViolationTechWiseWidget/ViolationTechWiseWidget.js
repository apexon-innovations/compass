import React from 'react'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import SliderWrapper from '../../components/SliderWrapper/SliderWrapper'
import style from './ViolationTechWiseWidget.module.scss'
import { TECH_LIST } from './ViolationTechWidgetFunction'

const ViolationTechWiseWidget = ({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
  return (
    <BracketBox animStatus="" boxTitle={'Violation'}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && (
          <div className={style.ViolationTechWise}>
            {/* <div className={style.violations}>
              <div className={[style.violationsInfo, style.red].join(' ')}>
                <div className={style.label}>Violation of Standards</div>
                <div className={style.number}>
                  <span className={style.span}>{response.data.totalViolations}</span>
                </div>
              </div>
            </div> */}
            {response.data && (
              <SliderWrapper
                title={'Technology Specific Violation'}
                sliderSettings={{ infinite: false }}
              >
                {techStack(response.data)}
              </SliderWrapper>
            )}
          </div>
        )}
      </WidgetContainer>
    </BracketBox>
  )
}

const techStack = data => {
  let language = data.language ? data.language.toUpperCase() : ''
  return (
    <div className={style.sliderDiv}>
      <div className={style.techIcon}>
        <img
          src={TECH_LIST[language] ? TECH_LIST[language] : TECH_LIST['NOTECH']}
          title=""
          alt=""
        />
      </div>
      <div className={style.techName}>{language}</div>
      <div className={style.techNumber}>{data.violations || 0}</div>
    </div>
  )
}

ViolationTechWiseWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default ViolationTechWiseWidget
