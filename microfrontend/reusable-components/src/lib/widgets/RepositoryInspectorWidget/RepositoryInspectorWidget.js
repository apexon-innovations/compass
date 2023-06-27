import React from 'react'
import PropTypes from 'prop-types'
import { Circle } from '@tiaanduplessis/react-progressbar'
import { colorCalculation, colorCalculationReverse } from '../../utils/commonFunction'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './RepositoryInspectorWidget.module.scss'

const RepositoryInspectorWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
  return (
    <div className={style.RepositoryInspectorWidget}>
      <BracketBox animStatus="" boxTitle={'Repository Inspector'}>
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response && response.data ? (
            <div className={style.repoInspectorInner}>
              <h2 className={style.sectionTitle}>Code Smell</h2>
              <div className={style.codeSmell}>
                <BoxWrapper label={'Size (LOC)'} value={response.data.size} />
                <BoxWrapper label={'Issues (#)'} value={response.data.issues} />
                <BoxWrapper label={'Complexity'} value={response.data.complexity} />
              </div>
              <div className={style.circleGraphs}>
                <CircleGraphWrapper
                  label={'Duplication'}
                  value={response.data.duplications}
                  status={colorCalculation(response.data.coverage)}
                />
                <CircleGraphWrapper
                  label={'Coverage'}
                  value={response.data.coverage}
                  status={colorCalculationReverse(response.data.coverage)}
                />
              </div>
              <h2 className={style.sectionTitle}>Technical Debt</h2>
              <div className={style.techDebts}>
                <BoxWrapper label={'Maintainability'} value={response.data.maintainability} />
                <BoxWrapper label={'Reliability'} value={response.data.reliability} />
                <BoxWrapper label={'Security'} value={response.data.security} />
              </div>
            </div>
          ) : (
            ''
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
})

const BoxWrapper = ({ label, value }) => {
  return (
    <div className={style.box}>
      <div className={style.label}>{label}</div>
      <div className={style.value}>{value}</div>
    </div>
  )
}

const CircleGraphWrapper = ({ value, label, status }) => {
  return (
    <div className={style.graph}>
      <Circle
        svgStyle={{
          transform: 'rotate(0)',
        }}
        text={{
          value: `${value}%`,
          style: {
            fontSize: '16px',
            fontWeight: '600',
            color: status.color,
            position: 'absolute',
            left: '50%',
            top: '50%',
            textAlign: 'center',
            padding: 0,
            margin: 0,
            transform: {
              prefix: true,
              value: 'translate(-50%, -50%)',
            },
          },
        }}
        progress={value / 100}
        color={status.color}
        trailColor={'#b1b1b1'}
        trailWidth={2}
        strokeWidth={6}
        duration={2000}
        easing="easeInOut"
      />
      <div className={[style.label, style[status.className]].join(' ')}>{label}</div>
    </div>
  )
}

BoxWrapper.propTypes = {
  label: PropTypes.any,
  value: PropTypes.any,
}

CircleGraphWrapper.propTypes = {
  value: PropTypes.any,
  label: PropTypes.any,
  status: PropTypes.any,
}

RepositoryInspectorWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default RepositoryInspectorWidget
