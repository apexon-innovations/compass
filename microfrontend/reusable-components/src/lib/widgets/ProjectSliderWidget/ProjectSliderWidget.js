import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withRouter } from '../../components/withRouter/withRouter'
import Slider from 'react-slick'
import { IC_SLR_ARR_LFT, IC_SLR_ARR_RGT, IC_SLR_ARC } from '../../const/imgConst.js'
import { getColorBasedOnCode } from '../../utils/commonFunction'
import { sortByNameFunction } from '../../utils/sortingFunction'
import { setInitialBoardAndRepoDataWithDispatchEvt } from '../../utils/projectDataStoreFunction'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import './ProjectSlider.scss'
import 'slick-carousel/slick/slick.css'

const ProjectSliderWidget = React.memo(
  ({ apiEndPointUrl, data, callback, projectId, navigate, location }) => {
    const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })

    const afterChange = e => {
      const pathname = location?.pathname.split('/') || []
      pathname.pop()
      const navigateUrl = pathname && pathname.length > 0 ? pathname.join('/') : ''
      setInitialBoardAndRepoDataWithDispatchEvt(response.data[e])
      navigate(`${navigateUrl}/${response.data[e].id}`)
    }

    let settings = {
      className: 'center',
      centerMode: true,
      infinite: true,
      centerPadding: '0',
      speed: 800,
      focusOnSelect: true,
      initialSlide: 0,
      dots: false,
      arrows: false,
      slidesToShow: 5,
      slidesToScroll: 1,
    }

    const [setting, setSetting] = useState(settings)
    const [sliderEle, setSliderEle] = useState(false)

    if (response && response.data && response.data.length > 0 && projectId !== '') {
      if (response.data.length <= 5) {
        setting.slidesToShow = response.data.length
      }

      response.data = sortByNameFunction(response.data, 'name')
      response.data.map((item, index) => {
        //Save Sprint ID in Local Storage
        if (item.id === projectId && setting.initialSlide === 0 && setting.initialSlide !== index) {
          setInitialBoardAndRepoDataWithDispatchEvt(item)
          setSetting(setting => ({ ...setting, initialSlide: index }))
        } else if (item.id === projectId) {
          setInitialBoardAndRepoDataWithDispatchEvt(item)
        }
        return item
      })
    }

    const slideIndexCurrentElement = (e, index) => {
      if (sliderEle && response.data.length <= 5) {
        setInitialBoardAndRepoDataWithDispatchEvt(response.data[index])
        sliderEle.slickGoTo(index)
      }
    }

    const refSlider = slider => {
      if (!sliderEle && slider) {
        setSliderEle(slider)
      }
    }

    return (
      <React.Fragment>
        <div
          className={[
            'projectSlider',
            response && response.data.length <= 5
              ? `limitProjects project${response.data.length}`
              : '',
          ].join(' ')}
        >
          <div className="sliderWrap">
            <WidgetContainer isLoading={isLoading} data={response} error={error}>
              {response && response.data && response.data.length > 0 && (
                <Slider ref={refSlider} {...setting} afterChange={afterChange.bind(this)}>
                  {response.data.map((item, index) => (
                    <div
                      className="sliderDiv"
                      key={item.id}
                      onClick={e => {
                        slideIndexCurrentElement(e, index)
                      }}
                    >
                      <div className="leftArrow">
                        <img src={IC_SLR_ARR_LFT} title="" alt="" />
                      </div>
                      <div
                        className={[
                          'projectBox',
                          getColorBasedOnCode(item.overallHealth).color,
                        ].join(' ')}
                      >
                        <div className={['icon', item.iconLocation ? 'whiteBg' : ''].join(' ')}>
                          {item.iconLocation ? (
                            <img src={item.iconLocation} title={item.name} alt={item.name} />
                          ) : (
                            <h6>{item.initials}</h6>
                          )}
                        </div>
                        <div className="ring">
                          <div className="top">
                            <img src={IC_SLR_ARC} title="" alt="" />
                          </div>
                          <div className="bottom">
                            <img src={IC_SLR_ARC} title="" alt="" />
                          </div>
                        </div>
                      </div>
                      <div className="rightArrow">
                        <img src={IC_SLR_ARR_RGT.default} title="" alt="" />
                      </div>
                    </div>
                  ))}
                </Slider>
              )}
              <div className="bgRays"></div>
            </WidgetContainer>
          </div>
        </div>
        <div className="projectSliderBg"></div>
      </React.Fragment>
    )
  },
)

ProjectSliderWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  projectId: PropTypes.string,
}

export default withRouter(ProjectSliderWidget)
