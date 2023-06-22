import React, { useRef } from 'react'
import PropTypes from 'prop-types'
import ModalClientInformation from './ModalClientInformation/ModalClientInformation'
import { getColorBasedOnCode } from '../../utils/commonFunction'
import JiraBoardDropDownWrapper from './JiraBoardDropDownWrapper'
import RepositoryDropDownWrapper from './RepositoryDropDownWrapper'
import WidgetContainer from '../../components/WidgetContainer/WidgetContainer'
import useWidgetAPIWrapper from '../../components/WidgetContainer/useWidgetAPIWrapper'
import ModalContainer from '../../components/ModalContainer/ModalContainer'
import style from './ProjectProgress.module.scss'

const responseMapFunction = response => {
  return response ? response.data : response
}

const ProjectProgressWidget = ({
  apiEndPointUrl,
  data,
  callback,
  fileUploaderUrl,
  popUpOpen,
  boardId,
  repoId,
  projectId,
  additionalClass,
  timeDuration,
}) => {
  const { response, error, isLoading } = useWidgetAPIWrapper({
    apiEndPointUrl,
    data,
    callback,
    responseMapFunction,
  })

  const modelRef = useRef()

  return (
    <div className={style.projectProgressMainBox}>
      <WidgetContainer isLoading={isLoading} error={error} data={response}>
        {response && Object.keys(response).length && (
          <React.Fragment>
            <div className="w-100 d-flex">
              <div
                className={[
                  style.projectProgress,
                  additionalClass ? style[additionalClass] : '',
                  'animated fadeInUp',
                ].join(' ')}
              >
                <div
                  onClick={() => {
                    modelRef.current.handleModelToggle()
                  }}
                  className={[
                    style.wrapBox,
                    style[
                      getColorBasedOnCode(
                        response.healthMetrics ? response.healthMetrics.overall : 'NA',
                      ).color
                    ],
                    'ml-2 align-items-center',
                  ].join(' ')}
                >
                  <div className={style.projectIcon}>
                    <div
                      className={[style.icon, response.iconLocation ? style.whiteBg : ''].join(' ')}
                    >
                      {response.iconLocation ? (
                        <img
                          src={response.iconLocation}
                          title={response.name}
                          alt={response.name}
                        />
                      ) : (
                        <h6>{response.initials}</h6>
                      )}
                    </div>
                    <div className={style.ring}></div>
                  </div>
                  <div className={style.name}>
                    <div className={style.cmpName}>{response.clientName}</div>
                    <div className={style.projName}>{response.name}</div>
                  </div>
                </div>
                {boardId ? (
                  <JiraBoardDropDownWrapper boardId={boardId} projectId={projectId} />
                ) : (
                  ''
                )}
                {repoId ? (
                  <RepositoryDropDownWrapper
                    repoId={repoId}
                    projectId={projectId}
                    timeDuration={timeDuration}
                  />
                ) : (
                  ''
                )}
              </div>
            </div>
            <ModalContainer size="large" ref={modelRef} isShow={popUpOpen}>
              <ModalClientInformation data={response} fileUploaderUrl={fileUploaderUrl} />
            </ModalContainer>
          </React.Fragment>
        )}
      </WidgetContainer>
    </div>
  )
}

ProjectProgressWidget.propTypes = {
  apiEndPointUrl: PropTypes.string,
  fileUploaderUrl: PropTypes.string,
  data: PropTypes.any,
  callback: PropTypes.func,
  popUpOpen: PropTypes.bool,
  boardId: PropTypes.string,
  repoId: PropTypes.any,
  projectId: PropTypes.string,
  additionalClass: PropTypes.string,
  timeDuration: PropTypes.number,
}

export default ProjectProgressWidget
