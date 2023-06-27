import React from 'react'
import SquareBox from '../../components/CommonComponents/SquareBox/SquareBox'
import PropTypes from 'prop-types'
import style from './SecurityComplianceScoreWidget.module.scss'

const SecurityComplianceScoreWidget = React.memo(({ repositoryName }) => {
  return (
    <SquareBox boxTitle={repositoryName ? repositoryName : ''}>
      <div className={`${style.securityScore} blurAll`}>
        <div className={style.row}>
          <div className={style.box}>
            <div className={style.label}>CWE</div>
            <div className={style.value}>180</div>
          </div>
          <div className={style.box}>
            <div className={style.label}>CISQ-Security</div>
            <div className={style.value}>201</div>
          </div>
          <div className={style.box}>
            <div className={style.label}>OWASP</div>
            <div className={style.value}>90</div>
          </div>
        </div>
        <div className={style.row}>
          <div className={style.box}>
            <div className={style.label}>GDPR</div>
            <div className={style.value}>145</div>
          </div>
          <div className={style.box}>
            <div className={style.label}>XSS</div>
            <div className={style.value}>85</div>
          </div>
        </div>
        <div className={style.row}>
          <div className={style.box}>
            <div className={style.label}>SQL Injection</div>
            <div className={style.value}>170</div>
          </div>
          <div className={style.box}>
            <div className={style.label}>Command Injection</div>
            <div className={style.value}>79</div>
          </div>
        </div>
      </div>
    </SquareBox>
  )
})

SecurityComplianceScoreWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  lineName: PropTypes.any,
  repositoryName: PropTypes.string,
}

export default SecurityComplianceScoreWidget
