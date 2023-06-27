import React, { useRef } from 'react'
import PropTypes from 'prop-types'
import ModalComplianceBox from './ModalComplianceBox'
import BracketBox from '../../components/CommonComponents/BracketBox/BracketBox'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import ModalContainer from '../../components/ModalContainer/ModalContainer'
import style from './ComplianceWidget.module.scss'
import {
  IC_COMPLIANCE_DONE,
  IC_COMPLIANCE_PENDING,
  IC_COMPLIANCE_NCOBSERVATION,
  IC_COMPLIANCE_NCS,
  IC_COMPLIANCE_OBSERVATION,
} from '../../const/imgConst.js'

const ComplianceSummerySection = ({ icon, label, modelRef }) => {
  return (
    <div
      className={[style.complianceBox, 'blurAll'].join(' ')}
      onClick={() => {
        modelRef.current.handleModelToggle()
      }}
    >
      <div className={style.statusIcon}>
        <img src={icon} title="" alt="" />
      </div>
      {label && <div className={style.statusText}>({label})</div>}
    </div>
  )
}
const ComplianceWidget = React.memo(({ apiEndPointUrl, data, callback }) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({ apiEndPointUrl, data, callback })
  const modelRef = useRef()

  const calculateCondition = complianceSummary => {
    const { isPending, openTickets, openObservations } = complianceSummary
    if (isPending) {
      return (
        <ComplianceSummerySection
          modelRef={modelRef}
          icon={IC_COMPLIANCE_PENDING}
          label={'No Audit Data'}
        />
      )
    } else if (!isPending && openTickets === 0 && openObservations === 0) {
      return <ComplianceSummerySection modelRef={modelRef} icon={IC_COMPLIANCE_DONE} />
    } else if (!isPending && openTickets > 0 && openObservations === 0) {
      return (
        <ComplianceSummerySection
          modelRef={modelRef}
          icon={IC_COMPLIANCE_NCS}
          label={`${openTickets} Tickets`}
        />
      )
    } else if (!isPending && openObservations > 0 && openTickets === 0) {
      return (
        <ComplianceSummerySection
          modelRef={modelRef}
          icon={IC_COMPLIANCE_OBSERVATION}
          label={`${openObservations} Observations`}
        />
      )
    } else if (!isPending && openObservations > 0 && openTickets > 0) {
      return (
        <ComplianceSummerySection
          modelRef={modelRef}
          icon={IC_COMPLIANCE_NCOBSERVATION}
          label={`${openTickets} Tickets & ${openObservations} Observations`}
        />
      )
    } else {
      return null
    }
  }

  return (
    <div className={style.complianceBoxOuter}>
      <BracketBox boxTitle={'Compliance'}>
        <WidgetContainer isLoading={isLoading} data={response} error={error}>
          {response && calculateCondition(response.data.complianceSummary)}
        </WidgetContainer>
      </BracketBox>
      <ModalContainer ref={modelRef}>
        <ModalComplianceBox response={response.data} />
      </ModalContainer>
    </div>
  )
})

ComplianceSummerySection.propTypes = {
  icon: PropTypes.string,
  label: PropTypes.any,
  modelRef: PropTypes.object,
}
ComplianceWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
}

export default ComplianceWidget
