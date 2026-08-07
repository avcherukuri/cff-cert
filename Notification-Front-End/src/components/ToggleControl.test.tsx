import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ToggleControl } from './ToggleControl';

describe('ToggleControl', () => {
  it('renders the label and reflects the checked state', () => {
    render(<ToggleControl id="emailEnabled" label="Email notifications" checked onChange={jest.fn()} />);

    const toggle = screen.getByRole('switch', { name: 'Email notifications' });
    expect(toggle).toBeChecked();
  });

  it('calls onChange with the new checked value when toggled', async () => {
    const onChange = jest.fn();
    render(<ToggleControl id="emailEnabled" label="Email notifications" checked={false} onChange={onChange} />);

    await userEvent.click(screen.getByRole('switch', { name: 'Email notifications' }));

    expect(onChange).toHaveBeenCalledWith(true);
  });

  it('disables the input when disabled is true', () => {
    render(<ToggleControl id="emailEnabled" label="Email notifications" checked disabled onChange={jest.fn()} />);

    expect(screen.getByRole('switch', { name: 'Email notifications' })).toBeDisabled();
  });

  it('renders no error alert and no aria-describedby when errorMessage is absent', () => {
    render(<ToggleControl id="emailEnabled" label="Email notifications" checked onChange={jest.fn()} />);

    expect(screen.queryByRole('alert')).not.toBeInTheDocument();
    expect(screen.getByRole('switch', { name: 'Email notifications' })).not.toHaveAttribute('aria-describedby');
  });

  it('wires errorMessage to an alert linked via aria-describedby', () => {
    render(
      <ToggleControl
        id="emailEnabled"
        label="Email notifications"
        checked
        onChange={jest.fn()}
        errorMessage="must not be null"
      />,
    );

    const toggle = screen.getByRole('switch', { name: 'Email notifications' });
    const alert = screen.getByRole('alert');
    expect(alert).toHaveTextContent('must not be null');
    expect(toggle).toHaveAttribute('aria-describedby', alert.id);
  });
});
