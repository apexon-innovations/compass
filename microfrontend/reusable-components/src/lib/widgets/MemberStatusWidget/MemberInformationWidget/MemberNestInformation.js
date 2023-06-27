import React from 'react'
import PropTypes from 'prop-types'
import { USER_DUMMY } from '../../../const/imgConst.js'
import dayjs from 'dayjs'
import utc from 'dayjs/plugin/utc'
import Slider from 'react-slick'
import WidgetContainer from '../../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../../components/WidgetContainer/useWidgetAPIWrapper'
import style from './MemberInformationWidget.module.scss'
import 'slick-carousel/slick/slick.css'

dayjs.extend(utc)

const nestResponseMap = response => {
  return response && response.data ? response.data : {}
}

const MemberNestInformation = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction: nestResponseMap,
  })

  const getSettings = leaveLength => {
    return {
      infinite: leaveLength > 4 ? true : false,
      speed: 800,
      focusOnSelect: true,
      dots: false,
      slidesToShow: 4,
      slidesToScroll: 1,
    }
  }

  const leaveStackData = leaves => {
    return leaves.map(leave => {
      return (
        leave.date && (
          <div>
            <div className={style.leaveBox}>
              <div className={style.month}>
                {dayjs(Number(leave.date))
                  .utc()
                  .format('MMM')}
              </div>
              <div className={style.date}>
                {dayjs(Number(leave.date))
                  .utc()
                  .format('DD')}
              </div>
            </div>
          </div>
        )
      )
    })
  }
  return (
    <WidgetContainer isLoading={isLoading} data={response} error={error}>
      {response && (
        <div className={[style.userDetails, 'border-right'].join(' ')}>
          <div className={style.userImageArea}>
            {/* <div className={[style.ratingNumber, style.green].join(' ')}>4.0</div> */}
            <div className={style.userImg}>
              <img src={response.dp ? response.dp : USER_DUMMY} title="" alt="" />
            </div>
          </div>
          <div className={style.userInfo}>
            <div className={style.name}>{response.name}</div>
            <div className={style.designation}>
              {response.designation ? response.designation : ''}
            </div>
          </div>
          <div
            className={[style.AllocationDetails, !response.allocation ? 'blurAll' : ''].join(' ')}
          >
            <div className={style.title}>Allocation:</div>
            <div className={style.billability}>
              {response.allocation && Math.round(response.allocation.billable)}% billable
            </div>
            {response.allocation && response.allocation.endDate ? (
              <div className={style.date}>
                Till{' '}
                {dayjs(response.allocation.endDate)
                  .utc()
                  .format('MMMM D, YYYY')}
              </div>
            ) : (
              ''
            )}
          </div>
          <div className={[style.leaveList, !response.leaves ? 'blurAll' : ''].join(' ')}>
            <div className={[style.leaveCount, !response.leaves ? 'blurAll' : ''].join(' ')}>
              Leaves <span>({response.leaves ? response.leaves.length : ''})</span>
            </div>
            {response.leaves && response.leaves.length > 0 && (
              <div className={style.leaveStack}>
                <div className={style.sliderWrap}>
                  <Slider {...getSettings(response.leaves.length)}>
                    {leaveStackData(response.leaves)}
                  </Slider>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </WidgetContainer>
  )
})

MemberNestInformation.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default MemberNestInformation
