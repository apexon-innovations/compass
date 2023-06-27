import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import CircleGraph from '../../components/CommonGraphs/CircleGraph/CircleGraph'
import style from './ScoreWidget.module.scss'

const ScoreWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })

  return (
    <BracketBox animStatus="" boxTitle={'Score'}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && response.data ? (
          <div className={style.scoreBox}>
            <Row>
              <Col>
                <CircleGraphWrapper
                  title={'Security'}
                  value={response.data.security}
                  color={getColor(response.data.security)}
                />
              </Col>
              <Col>
                <CircleGraphWrapper
                  title={'Efficiency'}
                  value={response.data.efficiency}
                  color={getColor(response.data.efficiency)}
                />
              </Col>
            </Row>
            <Row>
              <Col>
                <CircleGraphWrapper
                  title={'Robustness'}
                  value={response.data.robustness}
                  color={getColor(response.data.robustness)}
                />
              </Col>
            </Row>
          </div>
        ) : (
          ''
        )}
      </WidgetContainer>
    </BracketBox>
  )
})

const getColor = score => {
  if (score >= 0 && score < 3) {
    return '#d00010' //red
  } else if (score >= 3 && score < 4) {
    return '#ffb245' //amber
  } else {
    return '#24ff00' //green
  }
}

const CircleGraphWrapper = ({ title, value, color }) => {
  return (
    <div className={style.box}>
      <div className={style.title}>{title}</div>
      <div className={style.progressGraph}>
        <CircleGraph
          config={{
            value: value,
            styleColor: color,
            color: color,
            progress: Number(value) ? Number(value) * 20 : 0, //out of 100
          }}
        />
      </div>
    </div>
  )
}

ScoreWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

CircleGraphWrapper.propTypes = {
  title: PropTypes.string,
  value: PropTypes.number,
  color: PropTypes.string,
}
export default ScoreWidget
