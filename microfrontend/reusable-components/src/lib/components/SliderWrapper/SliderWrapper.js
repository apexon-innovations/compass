import React from 'react'
import PropTypes from 'prop-types'
import Slider from 'react-slick'
import style from './SliderWrapper.module.scss'
import 'slick-carousel/slick/slick.css'

const SliderWrapper = React.memo(({ title, sliderSettings = {}, children }) => {
  const settings = {
    infinite: true,
    speed: 800,
    focusOnSelect: false,
    dots: false,
    slidesToShow: 4,
    slidesToScroll: 1,
    ...sliderSettings,
  }
  return (
    <div className={style.sliderWrapper}>
      {title && <h3 className={style.sliderTitle}>{title}</h3>}
      <div className={style.sliderWrap}>
        <Slider {...settings}>{children}</Slider>
      </div>
    </div>
  )
})
SliderWrapper.propTypes = {
  title: PropTypes.string,
  sliderSettings: PropTypes.object,
  children: PropTypes.any,
}

export default SliderWrapper
