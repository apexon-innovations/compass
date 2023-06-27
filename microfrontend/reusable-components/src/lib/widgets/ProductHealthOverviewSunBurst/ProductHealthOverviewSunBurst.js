import React from 'react'
import PropTypes from 'prop-types'
import { Row, Col } from 'react-bootstrap'
import SunburstChart from 'sunburst-chart'
import { getColorCodeFromProjectName } from '../../utils/projectDataStoreFunction'
import { shadeColor } from '../../utils/commonFunction'
import LegendStory from './LegendStory'
import LegendDefect from './LegendDefect'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import CustomLegend from '../../components/CommonGraphSections/CustomLegend/CustomLegend'
import ZoomBox from '../../components/CommonComponents/ZoomBox/ZoomBox'
import style from './ProductHealthOverviewSunBurst.module.scss'

const responseMapFunction = response => {
  const responseData = {
    name: '',
    color: '#ffffff',
    children: [],
  }

  if (response && response.data) {
    for (let i = 0; i < response.data.length; i++) {
      const colorCode = getColorCodeFromProjectName(response.data[i].projectName)
      responseData.children.push({
        name: response.data[i].projectName,
        color: colorCode,
        children: [
          {
            name: `Story Planned (${response.data[i].totalStories})`,
            color: shadeColor(colorCode, -30),
            children: [
              {
                name: `Story Backlog (${response.data[i].backlogStories})`,
                color: '#34CB2E',
                size: response.data[i].backlogStories,
              },
              {
                name: `Story Completed (${response.data[i].completedStories})`,
                color: '#3D8D3D',
                size: response.data[i].completedStories,
              },
              {
                name: `Story To Do (${response.data[i].pendingStories})`,
                color: '#DED739',
                size: response.data[i].pendingStories,
              },
            ],
          },
          {
            name: `Defects (${response.data[i].totalDefects})`,
            color: shadeColor(colorCode, 30),
            children: [
              {
                name: `Defects Backlog (${response.data[i].backlogDefects})`,
                color: '#DE5E3F',
                size: response.data[i].backlogDefects,
              },
              {
                name: `Defects Resolved (${response.data[i].resolvedDefects})`,
                color: '#FFB200',
                size: response.data[i].resolvedDefects,
              },
              {
                name: `Defects Pending (${response.data[i].pendingDefects})`,
                color: '#FF7A00',
                size: response.data[i].pendingDefects,
              },
            ],
          },
        ],
      })
    }

    return { response, chartData: responseData }
  }

  return null
}

const InfoValue = () => {
  return (
    <div className="infoContent">
      <p>An overview of product health which presents the insights as of today:</p>
      <ul>
        <li>
          Total Story Points/Counts: Backlog Story Points, Completed Story Points, In progress Story
          Points (part of sprints).
        </li>
        <li>
          Total Defects Points/Counts: Backlog Defects Points/Counts and Resolved Defects
          Points/Counts, In Progress Defects Points/Counts.
        </li>
      </ul>
    </div>
  )
}

const ProductHealthOverviewSunBurst = React.memo(
  ({ apiEndPointUrl, data, callback, zoomEnable, bracketEnable }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({
      apiEndPointUrl,
      data,
      callback,
      responseMapFunction,
    })

    if (response) {
      const myChart = SunburstChart()
      setTimeout(() => {
        myChart
          .data(response.chartData)
          .width(450)
          .height(450)
          .size('size')
          .color('color')
          .showLabels(true)
          .radiusScaleExponent(1)(document.getElementById('ProductHealthOverviewSunBurst'))
      }, 10)
    }

    return (
      <div className="w-100">
        <Row>
          <Col md={12}>
            <BracketBox
              animStatus=""
              needBracketBox={bracketEnable ? true : false}
              boxTitle={'Product Health Overview'}
              infoEnable={true}
              toolTipId="infoPHOSB"
              infoValue={<InfoValue />}
            >
              <WidgetContainer isLoading={isLoading} data={response} error={error}>
                <ZoomBox zoomEnable={zoomEnable} redirectURL={'/client/client-product-health'}>
                  <div className={style.sunBurstWrapper}>
                    <div className={style.chartBoxed} id="ProductHealthOverviewSunBurst" />
                    <div className="w-100 d-flex flex-column justify-content-flex-start">
                      <CustomLegend payload={LegendStory} />
                      <CustomLegend payload={LegendDefect} />
                    </div>
                  </div>
                </ZoomBox>
              </WidgetContainer>
            </BracketBox>
          </Col>
        </Row>
      </div>
    )
  },
)

ProductHealthOverviewSunBurst.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  zoomEnable: PropTypes.bool,
  bracketEnable: PropTypes.bool,
}

export default ProductHealthOverviewSunBurst
