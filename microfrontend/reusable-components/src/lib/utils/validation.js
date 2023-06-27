export const emailValidation = inputValue => {
  // eslint-disable-next-line no-useless-escape
  const reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
  return reg.test(inputValue)
}

export const urlValidation = inputValue => {
  // eslint-disable-next-line no-useless-escape
  const reg = /^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)?$/
  return reg.test(inputValue)
}

export const checkEmpty = value => {
  return !value || value === undefined || value === '' || value.length === 0
}
