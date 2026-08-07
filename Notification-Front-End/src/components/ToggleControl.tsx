import React from 'react';

export interface ToggleControlProps {
  id: string;
  label: string;
  checked: boolean;
  onChange: (checked: boolean) => void;
  disabled?: boolean;
  errorMessage?: string;
}

export function ToggleControl({ id, label, checked, onChange, disabled, errorMessage }: ToggleControlProps): JSX.Element {
  const errorId = errorMessage ? `${id}-error` : undefined;

  return (
    <div className="toggle-control">
      <label htmlFor={id}>{label}</label>
      <input
        id={id}
        type="checkbox"
        role="switch"
        checked={checked}
        disabled={disabled}
        aria-checked={checked}
        aria-describedby={errorId}
        onChange={(event) => onChange(event.target.checked)}
      />
      {errorMessage && (
        <span id={errorId} role="alert">
          {errorMessage}
        </span>
      )}
    </div>
  );
}
