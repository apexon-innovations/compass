import React, { useState } from 'react'
import { Tabs, Tab } from 'react-bootstrap'
import PropTypes from 'prop-types'

const ParentTabSection = React.memo(({ defaultActiveKey, children, callback }) => {
  const [activeTab, setActiveTab] = useState(defaultActiveKey || 0)
  const [loadedTab, setLoadedTab] = useState([Number(activeTab)])

  const activeSelectedTab = e => {
    if (loadedTab.indexOf(e) === -1) {
      const updateTabs = [...loadedTab]
      updateTabs.push(Number(e))
      setLoadedTab(updateTabs)
    }
    if (callback) callback(e)
    setActiveTab(e)
  }
  return (
    <div className="w-100 d-flex h-100 flex-column">
      <Tabs
        defaultActiveKey={activeTab}
        id={'main' + activeTab}
        onSelect={e => activeSelectedTab(e)}
      >
        {children.map((item, index) => {
          return (
            <Tab key={index} eventKey={index} title={item.props.title || ''}>
              {activeTab === index || loadedTab.indexOf(Number(index)) > -1
                ? item.props.children
                : null}
            </Tab>
          )
        })}
      </Tabs>
    </div>
  )
})

export const CustomTab = ({ children }) => {
  return children
}

ParentTabSection.propTypes = {
  defaultActiveKey: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  children: PropTypes.any,
  callback: PropTypes.func,
}

export default ParentTabSection
