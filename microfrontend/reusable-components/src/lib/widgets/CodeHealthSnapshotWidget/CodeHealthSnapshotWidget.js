import React from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import { XMasonry, XBlock } from 'react-xmasonry'
import { ResponsiveContainer } from 'recharts/lib/component/ResponsiveContainer'
import { Treemap } from 'recharts/lib/chart/Treemap'
import { Tooltip } from 'recharts/lib/component/Tooltip'
import TooltipCodeHealthSnapshotWidget from './TooltipCodeHealthSnapshotWidget'
import { getStoredData } from '../../utils/projectDataStoreFunction'
import { shadeColor } from '../../utils/commonFunction'
import COLOR_CODE from '../../const/colorCode'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import style from './CodeHealthSnapshotWidget.module.scss'

const calculateAverage = (repository, projectShadowColor) => {
  const repositoryTemp = repository.map(repo => {
    return {
      name: repo.repoName,
      size: repo.totalLineOfCode || 1000,
      color: projectShadowColor,
      ...repo,
    }
  })
  return repositoryTemp
}

const getHeightWidth = totalRepoInProject => {
  let row
  let column
  for (let i = 1; i <= totalRepoInProject; i++) {
    const square = i * i
    if (square > totalRepoInProject) {
      row = i - 1
      if (totalRepoInProject > row * row) {
        column = i - 1 + (totalRepoInProject - row * row)
      } else {
        column = i - 1
      }

      if (column > 10) {
        column = 10
        row += column - 10
      }
      break
    }
  }
  return { height: row, width: column }
}

const CodeHealthSnapshotWidget = React.memo(({ apiEndPointUrl, data, callback, navigate }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
  let dataProject
  const projectData = getStoredData('PROJECT_CLIENT_DATA')
  if (response && response.data) {
    dataProject = [...response.data].map(item => {
      const project = projectData.find(project => project.id === item.id)
      const colorCode = project ? project.colorCode : COLOR_CODE[0]
      const projectShadowColor = shadeColor(colorCode || COLOR_CODE[0], -35)

      item.repos = calculateAverage(item.repos, colorCode)

      return {
        ...item,
        backGroundColor: colorCode || '',
        textColor: projectShadowColor,
        sizeDiv: getHeightWidth(item.repos.length),
      }
    })
  }

  return (
    <WidgetContainer isLoading={isLoading} data={dataProject} error={error}>
      <XMasonry targetBlockWidth={150} center={false}>
        {dataProject &&
          dataProject.map((item, index) => {
            return (
              <XBlock width={item.sizeDiv.width} key={index}>
                <div
                  key={index}
                  style={{ margin: '7px', 'background-color': item.backGroundColor }}
                  className={style.singleGraph}
                  onClick={() => {
                    navigate(`/client/business-health-overview/${item.id}`)
                  }}
                >
                  <h5 className={style.title}>{item.name}</h5>
                  <ResponsiveContainer minHeight={item.sizeDiv.height * 50} width={'100%'}>
                    <Treemap
                      height={item.sizeDiv.height * 50}
                      width={'100%'}
                      data={item.repos}
                      dataKey="size"
                      ratio={4 / 3}
                      fontSize={10}
                      fill={item.textColor}
                      stroke={'#ffffff'}
                      style={{
                        strokeWidth: 1,
                      }}
                    >
                      <Tooltip content={TooltipCodeHealthSnapshotWidget}></Tooltip>
                    </Treemap>
                  </ResponsiveContainer>
                </div>
              </XBlock>
            )
          })}
      </XMasonry>
    </WidgetContainer>
  )
})

CodeHealthSnapshotWidget.propTypes = {
  navigate: PropTypes.any,
  apiEndPointUrl: PropTypes.any,
  data: PropTypes.any,
  callback: PropTypes.any,
}

export default withRouter(CodeHealthSnapshotWidget)
