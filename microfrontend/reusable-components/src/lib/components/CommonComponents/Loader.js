import React from 'react'

const Loader = React.memo(() => {
  return (
    <div id="three-bar-loader-overlay">
      <div id="three-bar-loader" />
    </div>
  )
})

export default Loader
