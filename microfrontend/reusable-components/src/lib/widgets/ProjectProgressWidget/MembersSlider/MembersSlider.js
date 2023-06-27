import React, { Component } from 'react'
import PropTypes from 'prop-types'
import Slider from 'react-slick'
import { USER_DUMMY } from '../../../const/imgConst.js'
import style from './MembersSlider.module.scss'
import 'slick-carousel/slick/slick.css'

class MembersSlider extends Component {
  renderSlide(data) {
    return (
      data &&
      data.map((item, key) => {
        return (
          <div className={style.sliderDiv} key={key}>
            <div className={style.member}>
              <img src={item.dp ? item.dp : USER_DUMMY} title={item.name} alt={item.email} />
            </div>
          </div>
        )
      })
    )
  }

  render() {
    const { data } = this.props
    const settings = {
      infinite: data && data.length > 4 ? true : false,
      speed: 800,
      focusOnSelect: true,
      dots: false,
      slidesToShow: 4,
      slidesToScroll: 1,
    }

    return (
      <div className={style.membersSlider}>
        <div className={style.sliderWrap}>
          <Slider {...settings}>{this.renderSlide(data)}</Slider>
        </div>
      </div>
    )
  }
}

MembersSlider.propTypes = {
  data: PropTypes.any,
}

export default MembersSlider
