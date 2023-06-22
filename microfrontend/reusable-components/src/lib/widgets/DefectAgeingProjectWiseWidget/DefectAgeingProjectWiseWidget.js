import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import config from './DefectAgeingProjectWiseConfig.json'
import WidgetLegendsProject from './WidgetLegendsProject.json'
import TooltipDefectAgeing from '../DefectAgeingWidget/TooltipDefectAgeing'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import CommonLineChart from '../../components/CommonGraphs/CommonLineChart/CommonLineChart'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import style from './DefectAgeingProjectWiseWidget.module.scss'

const responseMapFunction = response => {
  if (response && response.data) {
    const range = [
      { name: '>100' },
      { name: '75-100' },
      { name: '51-75' },
      { name: '41-50' },
      { name: '31-40' },
      { name: '21-30' },
      { name: '11-20' },
      { name: '6-10' },
      { name: '3-5' },
      { name: '0-3' },
    ]
    response.data.range.map(item => {
      range.map(rangeItem => {
        if (rangeItem.name === item.value) {
          rangeItem[`Critical`] = item.critical
          rangeItem[`Blocker`] = item.blocker
          rangeItem[`Major`] = item.major
          rangeItem[`Minor`] = item.minor
          rangeItem[`Unattended`] = item.unattended
        }
        return rangeItem
      })
      return item
    })

    return { data: range }
  }
  return false
}

const DefectAgeingProjectWiseWidget = ({
  apiEndPointUrl,
  showHideEnable,
  data,
  callback,
  title,
  needBracketBox,
}) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: responseMapFunction,
  })

  const [activeAxisName, setActiveAxisName] = useState(false)

  const [btnShow, setBtnShow] = useState()

  const updateConfig = data => {
    setActiveAxisName(activeAxisName && activeAxisName === data.name ? false : data.name)
  }

  let updatedConfig = { ...config }
  if (activeAxisName) {
    updatedConfig.line = updatedConfig.line.filter(item => {
      return item.key === activeAxisName
    })
  }

  return (
    <div className={style.defectRatio}>
      <BracketBox
        hideShow={btnShow ? 'hide' : ''}
        needBracketBox={needBracketBox}
        boxTitle={title || 'Story Point Defect Ratio'}
      >
        <WidgetContainer isLoading={isLoading} error={error} data={response}>
          {response ? (
            <React.Fragment>
              {showHideEnable ? (
                <React.Fragment>
                  <div className="btnHideShow" onClick={() => setBtnShow(!btnShow)}>
                    {btnShow ? 'Show' : 'Hide'}
                  </div>
                </React.Fragment>
              ) : (
                <div className="controlRow">
                  <div className="graphNavBox">
                    <div className="caption">{response.data.projectName}</div>
                  </div>
                </div>
              )}
              <div className="toggleThings">
                <div className="clientWidgetInner">
                  <CommonLineChart
                    allData={{
                      config: updatedConfig,
                      data: showHideEnable ? [...response.data] : response.data,
                    }}
                    customToolTip={TooltipDefectAgeing}
                  />
                  <CustomLegend
                    payload={WidgetLegendsProject}
                    callback={updateConfig}
                    activeLegendName={activeAxisName}
                  />
                </div>
              </div>
            </React.Fragment>
          ) : (
            ''
          )}
        </WidgetContainer>
      </BracketBox>
    </div>
  )
}

DefectAgeingProjectWiseWidget.propTypes = {
  detailsUrl: PropTypes.string,
  apiEndPointUrl: PropTypes.string,
  showHideEnable: PropTypes.bool,
  data: PropTypes.any,
  callback: PropTypes.func,
  title: PropTypes.string,
  needBracketBox: PropTypes.bool,
}

export default withRouter(DefectAgeingProjectWiseWidget)
