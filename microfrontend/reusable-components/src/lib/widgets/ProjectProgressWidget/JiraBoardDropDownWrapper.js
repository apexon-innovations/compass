import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { sortByNameFunction } from '../../utils/sortingFunction'
import { setProjectBoardData, getStoredProjectData } from '../../utils/projectDataStoreFunction'
import CustomDropdown from '../../components/CommonComponents/CustomDropdown'
import style from './ProjectProgress.module.scss'

const responseMapBoardId = response => {
  return response
    ? response.map(item => {
        return { displayName: item.name, key: item.boardId }
      })
    : []
}

const JiraBoardDropDownWrapper = ({ boardId, projectId }) => {
  const projectData = getStoredProjectData()

  const projectExist =
    projectData.projects && projectData.projects.length > 0
      ? projectData.projects.find(project => project.projectId === projectId)
      : false

  const [selectedOption, setSelectedOption] = useState(false)
  const [selectedProject, setSelectedProject] = useState(projectExist)

  if (projectExist && projectExist.boards && !selectedOption) {
    projectExist['boardList'] = responseMapBoardId(projectExist.boards)
    const display = projectExist.boardList.find(board => board.key === boardId)
    setSelectedOption(display || {})
    setSelectedProject(projectExist)
  }

  const onSelect = boardIdKey => {
    setSelectedOption(selectedProject.boardList[boardIdKey])
    setProjectBoardData(
      { id: selectedProject.projectId, ...selectedProject },
      true,
      selectedProject.boardList[boardIdKey].key,
    )
  }

  return selectedProject && selectedProject.boards ? (
    <div className={style.customDropDownSpace}>
      <div className={style.roundDropDown}>
        <CustomDropdown
          items={sortByNameFunction(selectedProject.boardList, 'displayName')}
          alignRight={true}
          selectedOption={selectedOption.displayName}
          onSelectCallback={onSelect}
        />
      </div>
    </div>
  ) : (
    ''
  )
}

JiraBoardDropDownWrapper.propTypes = {
  boardId: PropTypes.string,
  projectId: PropTypes.string,
}

export default JiraBoardDropDownWrapper
