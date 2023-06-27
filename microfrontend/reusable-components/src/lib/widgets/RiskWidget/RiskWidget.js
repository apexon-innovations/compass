import React from 'react'
import PropTypes from 'prop-types'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import SliderWrapper from '../../components/SliderWrapper/SliderWrapper'
import style from './RiskWidget.module.scss'

const RiskWidget = ({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
  return (
    <BracketBox animStatus="" boxTitle={'Risk'}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && (
          <div className={style.riskBox}>
            <div className={style.statusNumbers}>
              <HeaderSectionComponent
                title={'Added Critical Violations'}
                value={response.data.violations.added || 0}
              />
              <HeaderSectionComponent
                title={'Removed Critical Violations'}
                value={response.data.violations.removed || 0}
              />
            </div>
            <div className="blurAll">
              <SliderWrapper title={'Top Risk Modules'} sliderSettings={{ infinite: false }}>
                {/* {riskBoxComponent(data)} */}
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.red].join(' ')}>
                    <div className={style.number}>376</div>
                  </div>
                  <div className={style.riskName}>Payment</div>
                </div>
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.red].join(' ')}>
                    <div className={style.number}>345</div>
                  </div>
                  <div className={style.riskName}>User</div>
                </div>
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.amber].join(' ')}>
                    <div className={style.number}>256</div>
                  </div>
                  <div className={style.riskName}>Dashoard</div>
                </div>
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.green].join(' ')}>
                    <div className={style.number}>120</div>
                  </div>
                  <div className={style.riskName}>Registration</div>
                </div>
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.red].join(' ')}>
                    <div className={style.number}>528</div>
                  </div>
                  <div className={style.riskName}>Test 01</div>
                </div>
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.amber].join(' ')}>
                    <div className={style.number}>295</div>
                  </div>
                  <div className={style.riskName}>Test 02</div>
                </div>
                <div className={style.sliderDiv}>
                  <div className={[style.numberBox, style.green].join(' ')}>
                    <div className={style.number}>86</div>
                  </div>
                  <div className={style.riskName}>Test 03</div>
                </div>
              </SliderWrapper>
            </div>
          </div>
        )}
      </WidgetContainer>
    </BracketBox>
  )
}

const HeaderSectionComponent = ({ title, value }) => {
  return (
    <div className={style.box}>
      <div className={style.title}>{title}</div>
      <div className={style.number}>{value}</div>
    </div>
  )
}

// const riskBoxComponent = data => {
//   return data.map((item, index) => {
//     return (
//       <div className={style.sliderDiv} key={index}>
//         <div className={[style.numberBox, style.red].join(' ')}>
//           <div className={style.number}>{item.value}</div>
//         </div>
//         <div className={style.riskName}>{item.name}</div>
//       </div>
//     )
//   })
// }

RiskWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

HeaderSectionComponent.propTypes = {
  title: PropTypes.string,
  value: PropTypes.any,
}

export default RiskWidget
