import React from 'react'
import Header from '../Header'
import { fireEvent, render, screen } from '@testing-library/react'
import { setIntoLocalStorage } from '../../../utils/loginFunction'
import { mockJest } from '../../../../__tests__/mockData'

describe('Header', () => {
  it('Header rendered correctly', () => {
    setIntoLocalStorage({
      accessToken:
        'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c3IiOiJkZXMuc2VydmljZUBhcGV4b24uY29tIiwiZXhwIjoxNjgzMTgyNTkzLCJ3ZGd0IjoicG1vX292ZXJ2aWV3X3N0YXR1cyxwbW9fb3ZlcnZpZXdfbWVtYmVyU3RhdHVzLHBtb19vdmVydmlld19ibG9ja2VycyxwbW9fb3ZlcnZpZXdfZWZmb3J0VmFyaWFuY2UscG1vX292ZXJ2aWV3X2Jhcm9tZXRlcixwbW9fb3ZlcnZpZXdfY29tcGxpYW5jZSxwbW9fb3ZlcnZpZXctZGVmZWN0c1RvdGFsQ291bnQscG1vX292ZXJ2aWV3X2RlZmVjdHNMb2dnZWRWc0FjY2VwdGVkLHBtb19vdmVydmlld19zcHJpbnRDYWxlbmRhclZpZXcscG1vX292ZXJ2aWV3X3Byb2plY3RIZWFsdGhNZXRyaWNzLHBtb19vcGVyYXRpb25hbF9zdG9yeVJlcG9ydHMscG1vX29wZXJhdGlvbmFsX3N0b3J5UG9pbnRzRGVsaXZlcmVkVnNBY2NlcHRlZCxwbW9fb3BlcmF0aW9uYWxfbnBzLHBtb19vcGVyYXRpb25hbF9iaWxsaW5nLHBtb19vcGVyYXRpb25hbF9zcHJpbnRCdXJuRG93bkNoYXJ0LHBtb19vcGVyYXRpb25hbF9zYXlEb1JhdGlvLHBtb19zdHJhdGVnaWNfcHJzTWFuYWdlbWVudCxwbW9fc3RyYXRlZ2ljX3N1Ym1pdHRlck1ldHJpY3MscG1vX3N0cmF0ZWdpY19yZXZpZXdlcnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfY29sbGFib3JhdGlvbnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfcXVhbGl0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY19jb21wbGlhbmNlLHBtb19zdHJhdGVnaWNfcmVwb3NpdG9yeUluc3BlY3RvcixwbW9fc3RyYXRlZ2ljX2NvZGVDaHVybkFuZExlZ2FjeUZhY3RvcixwbW9fc3RyYXRlZ2ljX21lbWJlcndpc2VBY3Rpdml0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY192aW9sYXRpb24scG1vX3N0cmF0ZWdpY19zY29yZSxwbW9fc3RyYXRlZ2ljX3Jpc2sscG1vX3N0cmF0ZWdpY19zZWN1cml0eVNjb3JlLGNsaWVudF9vcGVyYXRpb25hbF9wcm9kdWN0SGVhbHRoT3ZlcnZpZXcsY2xpZW50X29wZXJhdGlvbmFsX29wZW5EZWZlY3RzU3VtbWFyeSxjbGllbnRfb3BlcmF0aW9uYWxfc3RvcnlQb2ludHNEZWZlY3RzUmF0aW8sY2xpZW50X29wZXJhdGlvbmFsX3Byb2plY3RDb21wbGV0aW9uVHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF9zdG9yeVBvaW50c0RlbGl2ZXJ5VHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF92ZWxvY2l0eVRyZW5kcyxjbGllbnRfYnVzaW5lc3NfY29kZUhlYWx0aCxjbGllbnRfb3BlcmF0aW9uYWxfZGVmZWN0QWdlaW5nLHBkb19vdmVyYWxsSGVhbHRoIiwicm9sIjoiUFJPSkVDVF9NQU5BR0VSLFBST0pFQ1RfQ0xJRU5ULFBETyJ9.hn93PepXXvdO-qdz35sajVX1-j6uC58jSB83kCSGve8',
      refreshToken:
        'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c3IiOiJkZXMuc2VydmljZUBhcGV4b24uY29tIiwiZXhwIjoxNjgzMjAyMzkzLCJ3ZGd0IjoicG1vX292ZXJ2aWV3X3N0YXR1cyxwbW9fb3ZlcnZpZXdfbWVtYmVyU3RhdHVzLHBtb19vdmVydmlld19ibG9ja2VycyxwbW9fb3ZlcnZpZXdfZWZmb3J0VmFyaWFuY2UscG1vX292ZXJ2aWV3X2Jhcm9tZXRlcixwbW9fb3ZlcnZpZXdfY29tcGxpYW5jZSxwbW9fb3ZlcnZpZXctZGVmZWN0c1RvdGFsQ291bnQscG1vX292ZXJ2aWV3X2RlZmVjdHNMb2dnZWRWc0FjY2VwdGVkLHBtb19vdmVydmlld19zcHJpbnRDYWxlbmRhclZpZXcscG1vX292ZXJ2aWV3X3Byb2plY3RIZWFsdGhNZXRyaWNzLHBtb19vcGVyYXRpb25hbF9zdG9yeVJlcG9ydHMscG1vX29wZXJhdGlvbmFsX3N0b3J5UG9pbnRzRGVsaXZlcmVkVnNBY2NlcHRlZCxwbW9fb3BlcmF0aW9uYWxfbnBzLHBtb19vcGVyYXRpb25hbF9iaWxsaW5nLHBtb19vcGVyYXRpb25hbF9zcHJpbnRCdXJuRG93bkNoYXJ0LHBtb19vcGVyYXRpb25hbF9zYXlEb1JhdGlvLHBtb19zdHJhdGVnaWNfcHJzTWFuYWdlbWVudCxwbW9fc3RyYXRlZ2ljX3N1Ym1pdHRlck1ldHJpY3MscG1vX3N0cmF0ZWdpY19yZXZpZXdlcnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfY29sbGFib3JhdGlvbnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfcXVhbGl0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY19jb21wbGlhbmNlLHBtb19zdHJhdGVnaWNfcmVwb3NpdG9yeUluc3BlY3RvcixwbW9fc3RyYXRlZ2ljX2NvZGVDaHVybkFuZExlZ2FjeUZhY3RvcixwbW9fc3RyYXRlZ2ljX21lbWJlcndpc2VBY3Rpdml0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY192aW9sYXRpb24scG1vX3N0cmF0ZWdpY19zY29yZSxwbW9fc3RyYXRlZ2ljX3Jpc2sscG1vX3N0cmF0ZWdpY19zZWN1cml0eVNjb3JlLGNsaWVudF9vcGVyYXRpb25hbF9wcm9kdWN0SGVhbHRoT3ZlcnZpZXcsY2xpZW50X29wZXJhdGlvbmFsX29wZW5EZWZlY3RzU3VtbWFyeSxjbGllbnRfb3BlcmF0aW9uYWxfc3RvcnlQb2ludHNEZWZlY3RzUmF0aW8sY2xpZW50X29wZXJhdGlvbmFsX3Byb2plY3RDb21wbGV0aW9uVHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF9zdG9yeVBvaW50c0RlbGl2ZXJ5VHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF92ZWxvY2l0eVRyZW5kcyxjbGllbnRfYnVzaW5lc3NfY29kZUhlYWx0aCxjbGllbnRfb3BlcmF0aW9uYWxfZGVmZWN0QWdlaW5nLHBkb19vdmVyYWxsSGVhbHRoIiwicm9sIjoiUFJPSkVDVF9NQU5BR0VSLFBST0pFQ1RfQ0xJRU5ULFBETyJ9.c7MZKrZHXga3IdGJZajwaTa31dQOPsP6BzccXlnbgoY',
      expiresWithin: 30,
    })
    render(<Header isSearchBox={true} />)
    mockJest(
      200,
      JSON.stringify({
        data: [
          {
            id: '5e7db6f7215e091c132c8f84',
            name: 'Navigating Cancer Care',
            initials: 'N C C',
            category: 'Devlopment',
            iconLocation: "iconLocation.jpg",
            overallHealth: 'A',
            jiraProjectId: null,
            boards: null,
            repos: null,
          },
        ],
      }),
    )
    fireEvent.change(screen.getByPlaceholderText('Search Project'), {
      target: { value: 'Compass' },
    })
  })

  it('Header rendered correctly', async () => {
    const useRefSpy = jest
      .spyOn(React, 'useRef')
      .mockReturnValueOnce({ current: document.createElement('div') })
    document.addEventListener = jest.fn((event, cb) => {
      if (event === 'mousedown') {
        cb({ target: document.createElement('p') })
      }
    })
    render(<Header isSearchBox={true} />)
    expect(useRefSpy).toBeCalled()
  })
})
