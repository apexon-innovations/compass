import React, { useState } from 'react'
import PropTypes from 'prop-types'
import MultiSelect from '@khanacademy/react-multi-select'
import CustomDropdown from '../../components/CommonComponents/CustomDropdown'
import { sortByNameFunction } from '../../utils/sortingFunction'
import { timeDurationOptions } from '../../const/dataConst'
import {
  setProjectRepoData,
  getStoredProjectData,
  setProjectAnyData,
} from '../../utils/projectDataStoreFunction'
import style from './ProjectProgress.module.scss'

const responseMapBoardId = response => {
  return response
    ? response.map(item => {
        return { label: item.repoName, value: item.repoId }
      })
    : []
}

const RepositoryDropDownWrapper = ({ repoId, projectId, timeDuration }) => {
  const projectData = getStoredProjectData()
  const projectExist =
    projectData.projects && projectData.projects.length > 0
      ? projectData.projects.find(project => project.projectId === projectId)
      : false
  let defaultSelectedTimeDuration = timeDurationOptions
  if (timeDuration) {
    defaultSelectedTimeDuration = timeDurationOptions.filter(option => option.key === timeDuration)
  }
  const [selectedOption, setSelectedOption] = useState([])
  const [selectedTimeDuration, setSelectedTimeDuration] = useState(
    defaultSelectedTimeDuration[0].displayName,
  )

  const [selectedProject, setSelectedProject] = useState(false)

  if (projectExist && projectExist.repos && selectedOption.length === 0) {
    const mapResponse = sortByNameFunction(projectExist.repos, 'repoName')
    projectExist['repoList'] = responseMapBoardId(mapResponse)
    if (repoId) {
      setSelectedOption(repoId)
    }
    setSelectedProject(projectExist)
  }

  const onSelect = selected => {
    if (selected.length !== 0) {
      setSelectedOption(selected)
      setProjectRepoData({ id: selectedProject.projectId, ...selectedProject }, true, selected)
    } else {
      setSelectedOption([])
      setProjectRepoData({ id: selectedProject.projectId, ...selectedProject }, true, [
        selectedProject.repos[0].repoId,
      ])
    }
  }

  const onSelectTimeDuration = index => {
    setSelectedTimeDuration(timeDurationOptions[index].displayName)
    setProjectAnyData({ id: selectedProject.projectId, ...selectedProject }, true, {
      timeDuration: timeDurationOptions[index].key,
    })
  }

  return selectedProject && selectedProject.repoList ? (
    <React.Fragment>
      <div className={style.customDropDownSpace}>
        <div className={style.roundDropDown}>
          <div className={['bordered transparentMultiSelect'].join(' ')}>
            <MultiSelect
              options={selectedProject.repoList}
              selected={selectedOption}
              onSelectedChanged={onSelect}
              valueRenderer={selected => {
                if (selected.length === 0) {
                  return 'Select Repository'
                } else {
                  setSelectedOption(selected)
                  return `(${selected.length})`
                }
              }}
              overrideStrings={{
                selectSomeItems: 'Select Repository',
                allItemsAreSelected: 'All Repository are Selected',
                selectAll: 'Select All Repository',
                search: 'Search Repository',
              }}
              alignRight={true}
              bordered={true}
            />
          </div>
        </div>
        <div className={[style.roundDropDown, style.marginLeft10].join(' ')}>
          <CustomDropdown
            items={timeDurationOptions}
            alignRight={true}
            selectedOption={selectedTimeDuration}
            onSelectCallback={onSelectTimeDuration}
          />
        </div>
      </div>
    </React.Fragment>
  ) : (
    ''
  )
}

RepositoryDropDownWrapper.propTypes = {
  repoId: PropTypes.any,
  projectId: PropTypes.string,
  timeDuration: PropTypes.number,
}

export default RepositoryDropDownWrapper
