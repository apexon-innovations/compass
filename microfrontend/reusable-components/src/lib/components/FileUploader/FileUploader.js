import React from 'react'
import PropTypes from 'prop-types'

class FileUploader extends React.PureComponent {
  constructor(props) {
    super(props)
    this.fileInput = React.createRef()
  }

  handleChange(e) {
    // get the files
    let files = e.target.files
    const that = this
    for (let i = 0; i < files.length; i++) {
      let file = files[i]
      if (this.props.base64) {
        // Make new FileReader
        let reader = new FileReader()
        // Convert the file to base64 text
        reader.readAsDataURL(file)
        reader.onload = () => {
          // Make a fileInfo Object
          const fileInfo = {
            name: file.name,
            type: file.type,
            size: Math.round(file.size / 1000) + ' kB',
            base64: reader.result,
            file: file,
          }

          // Image Height Width Validation Commenting For demo

          // const image = new Image();
          // image.src = e.target.result;
          // image.onload = function () {
          // const height = this.height;
          // const width = this.width;
          if (Math.round(file.size / 1000) > 1024) {
            that.props.onError({ code: 'fileSize', msg: 'File size is more then 1 MB' })
            return true
          } else {
            that.props.onCompleted(fileInfo)
          }
          // };
        }
      } else {
        that.props.onCompleted(files[i])
      }
    }
  }

  render() {
    return (
      <input
        type="file"
        className={this.props.inputClassName}
        onChange={e => {
          this.handleChange(e)
        }}
        accept={this.props.accept}
        ref={this.fileInput}
        name={this.props.name}
      />
    )
  }
}

FileUploader.propTypes = {
  accept: PropTypes.string,
  name: PropTypes.string,
  inputClassName: PropTypes.string,
  onError: PropTypes.func,
  onCompleted: PropTypes.func,
  base64: PropTypes.bool,
}

FileUploader.defaultProps = {
  accept: 'image/png, image/jpeg, image/svg+xml',
  inputClassName: '',
  name: 'fileUploader',
  base64: true,
}

export default FileUploader
