import React from 'react'
import LoginForm from '../LoginForm'
import { fireEvent, render, screen, waitFor } from '@testing-library/react'
import { mockJest } from '__tests__/mockData'

jest.useFakeTimers()

describe('src/screens/Login/Widgets/LoginForm/LoginForm.js', () => {
  it('test handleLogin API Call successs', async () => {
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<LoginForm navigate={mockFn} apiEndPointUrl='http://testUrl/' />)
    })
    mockJest(200, JSON.stringify({
      "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c3IiOiJkZXMuc2VydmljZUBhcGV4b24uY29tIiwiZXhwIjoxNjgzMTgyNTkzLCJ3ZGd0IjoicG1vX292ZXJ2aWV3X3N0YXR1cyxwbW9fb3ZlcnZpZXdfbWVtYmVyU3RhdHVzLHBtb19vdmVydmlld19ibG9ja2VycyxwbW9fb3ZlcnZpZXdfZWZmb3J0VmFyaWFuY2UscG1vX292ZXJ2aWV3X2Jhcm9tZXRlcixwbW9fb3ZlcnZpZXdfY29tcGxpYW5jZSxwbW9fb3ZlcnZpZXctZGVmZWN0c1RvdGFsQ291bnQscG1vX292ZXJ2aWV3X2RlZmVjdHNMb2dnZWRWc0FjY2VwdGVkLHBtb19vdmVydmlld19zcHJpbnRDYWxlbmRhclZpZXcscG1vX292ZXJ2aWV3X3Byb2plY3RIZWFsdGhNZXRyaWNzLHBtb19vcGVyYXRpb25hbF9zdG9yeVJlcG9ydHMscG1vX29wZXJhdGlvbmFsX3N0b3J5UG9pbnRzRGVsaXZlcmVkVnNBY2NlcHRlZCxwbW9fb3BlcmF0aW9uYWxfbnBzLHBtb19vcGVyYXRpb25hbF9iaWxsaW5nLHBtb19vcGVyYXRpb25hbF9zcHJpbnRCdXJuRG93bkNoYXJ0LHBtb19vcGVyYXRpb25hbF9zYXlEb1JhdGlvLHBtb19zdHJhdGVnaWNfcHJzTWFuYWdlbWVudCxwbW9fc3RyYXRlZ2ljX3N1Ym1pdHRlck1ldHJpY3MscG1vX3N0cmF0ZWdpY19yZXZpZXdlcnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfY29sbGFib3JhdGlvbnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfcXVhbGl0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY19jb21wbGlhbmNlLHBtb19zdHJhdGVnaWNfcmVwb3NpdG9yeUluc3BlY3RvcixwbW9fc3RyYXRlZ2ljX2NvZGVDaHVybkFuZExlZ2FjeUZhY3RvcixwbW9fc3RyYXRlZ2ljX21lbWJlcndpc2VBY3Rpdml0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY192aW9sYXRpb24scG1vX3N0cmF0ZWdpY19zY29yZSxwbW9fc3RyYXRlZ2ljX3Jpc2sscG1vX3N0cmF0ZWdpY19zZWN1cml0eVNjb3JlLGNsaWVudF9vcGVyYXRpb25hbF9wcm9kdWN0SGVhbHRoT3ZlcnZpZXcsY2xpZW50X29wZXJhdGlvbmFsX29wZW5EZWZlY3RzU3VtbWFyeSxjbGllbnRfb3BlcmF0aW9uYWxfc3RvcnlQb2ludHNEZWZlY3RzUmF0aW8sY2xpZW50X29wZXJhdGlvbmFsX3Byb2plY3RDb21wbGV0aW9uVHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF9zdG9yeVBvaW50c0RlbGl2ZXJ5VHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF92ZWxvY2l0eVRyZW5kcyxjbGllbnRfYnVzaW5lc3NfY29kZUhlYWx0aCxjbGllbnRfb3BlcmF0aW9uYWxfZGVmZWN0QWdlaW5nLHBkb19vdmVyYWxsSGVhbHRoIiwicm9sIjoiUFJPSkVDVF9NQU5BR0VSLFBST0pFQ1RfQ0xJRU5ULFBETyJ9.hn93PepXXvdO-qdz35sajVX1-j6uC58jSB83kCSGve8",
      "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c3IiOiJkZXMuc2VydmljZUBhcGV4b24uY29tIiwiZXhwIjoxNjgzMjAyMzkzLCJ3ZGd0IjoicG1vX292ZXJ2aWV3X3N0YXR1cyxwbW9fb3ZlcnZpZXdfbWVtYmVyU3RhdHVzLHBtb19vdmVydmlld19ibG9ja2VycyxwbW9fb3ZlcnZpZXdfZWZmb3J0VmFyaWFuY2UscG1vX292ZXJ2aWV3X2Jhcm9tZXRlcixwbW9fb3ZlcnZpZXdfY29tcGxpYW5jZSxwbW9fb3ZlcnZpZXctZGVmZWN0c1RvdGFsQ291bnQscG1vX292ZXJ2aWV3X2RlZmVjdHNMb2dnZWRWc0FjY2VwdGVkLHBtb19vdmVydmlld19zcHJpbnRDYWxlbmRhclZpZXcscG1vX292ZXJ2aWV3X3Byb2plY3RIZWFsdGhNZXRyaWNzLHBtb19vcGVyYXRpb25hbF9zdG9yeVJlcG9ydHMscG1vX29wZXJhdGlvbmFsX3N0b3J5UG9pbnRzRGVsaXZlcmVkVnNBY2NlcHRlZCxwbW9fb3BlcmF0aW9uYWxfbnBzLHBtb19vcGVyYXRpb25hbF9iaWxsaW5nLHBtb19vcGVyYXRpb25hbF9zcHJpbnRCdXJuRG93bkNoYXJ0LHBtb19vcGVyYXRpb25hbF9zYXlEb1JhdGlvLHBtb19zdHJhdGVnaWNfcHJzTWFuYWdlbWVudCxwbW9fc3RyYXRlZ2ljX3N1Ym1pdHRlck1ldHJpY3MscG1vX3N0cmF0ZWdpY19yZXZpZXdlcnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfY29sbGFib3JhdGlvbnNNZXRyaWNzLHBtb19zdHJhdGVnaWNfcXVhbGl0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY19jb21wbGlhbmNlLHBtb19zdHJhdGVnaWNfcmVwb3NpdG9yeUluc3BlY3RvcixwbW9fc3RyYXRlZ2ljX2NvZGVDaHVybkFuZExlZ2FjeUZhY3RvcixwbW9fc3RyYXRlZ2ljX21lbWJlcndpc2VBY3Rpdml0eU1ldHJpY3MscG1vX3N0cmF0ZWdpY192aW9sYXRpb24scG1vX3N0cmF0ZWdpY19zY29yZSxwbW9fc3RyYXRlZ2ljX3Jpc2sscG1vX3N0cmF0ZWdpY19zZWN1cml0eVNjb3JlLGNsaWVudF9vcGVyYXRpb25hbF9wcm9kdWN0SGVhbHRoT3ZlcnZpZXcsY2xpZW50X29wZXJhdGlvbmFsX29wZW5EZWZlY3RzU3VtbWFyeSxjbGllbnRfb3BlcmF0aW9uYWxfc3RvcnlQb2ludHNEZWZlY3RzUmF0aW8sY2xpZW50X29wZXJhdGlvbmFsX3Byb2plY3RDb21wbGV0aW9uVHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF9zdG9yeVBvaW50c0RlbGl2ZXJ5VHJlbmRzLGNsaWVudF9vcGVyYXRpb25hbF92ZWxvY2l0eVRyZW5kcyxjbGllbnRfYnVzaW5lc3NfY29kZUhlYWx0aCxjbGllbnRfb3BlcmF0aW9uYWxfZGVmZWN0QWdlaW5nLHBkb19vdmVyYWxsSGVhbHRoIiwicm9sIjoiUFJPSkVDVF9NQU5BR0VSLFBST0pFQ1RfQ0xJRU5ULFBETyJ9.c7MZKrZHXga3IdGJZajwaTa31dQOPsP6BzccXlnbgoY",
      "expiresWithin": 30
    }));
    jest.advanceTimersByTime(3000)

    fireEvent.keyPress(screen.getByPlaceholderText('Enter Email ID'), { key: 'Enter' });
    fireEvent.change(screen.getByPlaceholderText('Enter Email ID'), { target: { value: 'demo@apexon.com' } });
    fireEvent.click(screen.getByPlaceholderText('Enter Email ID'));

    fireEvent.click(screen.getAllByText('Press Enter To Continue')[0]);

    fireEvent.change(screen.getByPlaceholderText('Password'), { target: { value: 'password' } });
    fireEvent.click(screen.getByPlaceholderText('Password'));

    fireEvent.click(screen.getAllByText('Press Enter To Continue')[1]);

    await waitFor(() => {
      expect(mockFn).toBeCalledWith('/home');
    })
  })

  it('test handleLogin API Call successs is false', async () => {
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<LoginForm navigate={mockFn} apiEndPointUrl='http://testUrl/' />)
    })
    mockJest(403, JSON.stringify({}))
    jest.advanceTimersByTime(3000)

    fireEvent.keyPress(screen.getByPlaceholderText('Enter Email ID'), { key: 'Enter' });
    fireEvent.change(screen.getByPlaceholderText('Enter Email ID'), { target: { value: 'demo@apexon.com' } });
    fireEvent.click(screen.getByPlaceholderText('Enter Email ID'));

    fireEvent.click(screen.getAllByText('Press Enter To Continue')[0]);

    fireEvent.change(screen.getByPlaceholderText('Password'), { target: { value: 'password' } });
    fireEvent.click(screen.getByPlaceholderText('Password'));

    fireEvent.click(screen.getAllByText('Press Enter To Continue')[1]);

  })

  it('test handleLogin API Call when email is not entered', async () => {
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<LoginForm navigate={mockFn} apiEndPointUrl='http://testUrl/' />)
    })
    fireEvent.click(screen.getAllByText('Press Enter To Continue')[1]);
  })

  it('test handleLogin API Call when password is not entered', async () => {
    const mockFn = jest.fn();
    await waitFor(() => {
      render(<LoginForm navigate={mockFn} apiEndPointUrl='http://testUrl/' />)
    })
    fireEvent.change(screen.getByPlaceholderText('Enter Email ID'), { target: { value: 'demo@apexon.com' } });
    fireEvent.click(screen.getByPlaceholderText('Enter Email ID'));

    fireEvent.click(screen.getAllByText('Press Enter To Continue')[0]);

    fireEvent.click(screen.getAllByText('Press Enter To Continue')[1]);
  })
})
